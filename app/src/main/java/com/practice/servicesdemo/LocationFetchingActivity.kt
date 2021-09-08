package com.practice.servicesdemo

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.practice.servicesdemo.databinding.ActivityLocationFetchingBinding
import com.practice.servicesdemo.services.LocationService
import com.practice.servicesdemo.util.Constants.Companion.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
import com.practice.servicesdemo.util.Constants.Companion.EXTRA_LOCATION
import com.practice.servicesdemo.util.Constants.Companion.LOCATION_REQUEST_CODE
import com.practice.servicesdemo.util.Utils.getLocationPermission
import com.practice.servicesdemo.util.Utils.isLocationPermissionGranted

class LocationFetchingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLocationFetchingBinding
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
    private lateinit var locationServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationFetchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        locationServiceIntent = Intent(this, LocationService::class.java)

        binding.getBt.setOnClickListener(this)
        binding.endBt.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.getBt -> {
                getLocationFromService()
            }
            binding.endBt -> {
                stopLocationService()
            }
        }
    }

    private fun startLocationService() {
        startService(locationServiceIntent)
    }

    private fun stopLocationService() {
        stopService(locationServiceIntent)
    }

    private fun getLocationFromService() {
        if (isLocationPermissionGranted()) {
            startLocationService()
        } else {
            getLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (isLocationPermissionGranted()) {
                startLocationService()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()
    }

    override fun onPause() {
        unregisterBroadcastReceiver()
        super.onPause()
    }

    private fun registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    private fun unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
    }

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                EXTRA_LOCATION
            )
            Log.d("Locations", "Activity: $location")
            if (location != null) {
                logResultsToScreen("${location.latitude} ${location.longitude} ")
            }
        }
    }

    private fun logResultsToScreen(output: String) {
        val outputWithPreviousLogs = "$output\n${binding.displayLocation.text}"
        binding.displayLocation.text = outputWithPreviousLogs
    }
}