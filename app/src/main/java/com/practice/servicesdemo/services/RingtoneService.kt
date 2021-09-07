package com.practice.servicesdemo.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class RingtoneService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(
            this, Settings.System.DEFAULT_RINGTONE_URI
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        
        mediaPlayer.apply {
            isLooping = true
            start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null


}