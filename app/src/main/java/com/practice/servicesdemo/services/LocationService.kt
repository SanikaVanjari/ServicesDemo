package com.practice.servicesdemo.services

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.practice.servicesdemo.MainActivity
import com.practice.servicesdemo.R
import com.practice.servicesdemo.util.Constants.Companion.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
import com.practice.servicesdemo.util.Constants.Companion.CHANNEL_ID
import com.practice.servicesdemo.util.Constants.Companion.CHANNEL_NAME
import com.practice.servicesdemo.util.Constants.Companion.EXTRA_LOCATION
import com.practice.servicesdemo.util.Constants.Companion.LOCATION_SERVICE_NOTIFICATION_ID
import com.practice.servicesdemo.util.Constants.Companion.SERVICE_LOCATION_REQUEST_CODE
import com.practice.servicesdemo.util.Constants.Companion.UPDATE_INTERVAL_IN_MILLISECONDS
import com.practice.servicesdemo.util.Utils


class LocationService : Service() {

    //region data
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        initLocationData();
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        displayNotification()
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient.requestLocationUpdates(
            this.locationRequest,
            this.locationCallback, Looper.myLooper()
        );
    }

    private fun displayNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = createNotification(getString(R.string.notification_description))

        startForeground(LOCATION_SERVICE_NOTIFICATION_ID, notification)
    }

    private fun createNotification(description: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, SERVICE_LOCATION_REQUEST_CODE, notificationIntent, 0, null
        )
        return NotificationCompat.Builder(
            this, CHANNEL_ID
        ).apply {
            setContentTitle(description)
            setOnlyAlertOnce(true)
            setSmallIcon(R.mipmap.ic_launcher)
            setContentIntent(pendingIntent)
        }.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null


    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val currentLocation: Location = locationResult.lastLocation

            val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
            intent.putExtra(EXTRA_LOCATION, currentLocation)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

            notificationManager.notify(
                LOCATION_SERVICE_NOTIFICATION_ID,
                createNotification(Utils.getLocationText(currentLocation))
            )

            Log.d(
                "Locations",
                currentLocation.latitude.toString() + "," + currentLocation.longitude
            )
        }
    }

    private fun initLocationData() {
        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS.toLong()
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

}