package com.practice.servicesdemo.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat


object Utils {

    /**
     * Returns the `location` object as a human readable string.
     * @param location  The [Location].
     */
    fun getLocationText(location: Location?): String{
        return if (location == null) "Unknown location" else "(" + location.latitude
            .toString() + ", " + location.longitude.toString() + ")"
    }

    fun Context.getLocationPermission() {
        ActivityCompat.requestPermissions(
            this as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constants.LOCATION_REQUEST_CODE
        )
    }

    fun Context.isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}