package com.practice.servicesdemo.util

class Constants {
    companion object {
        const val LOCATION_REQUEST_CODE = 1
        const val CHANNEL_ID = "2"
        const val CHANNEL_NAME = "LOCATION SERVICE CHANNEL"
        const val SERVICE_LOCATION_REQUEST_CODE = 3
        const val LOCATION_SERVICE_NOTIFICATION_ID = 4
        const val UPDATE_INTERVAL_IN_MILLISECONDS = 3000
        const val PACKAGE_NAME = "com.practice.servicesdemo.services"
        const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
    }
}