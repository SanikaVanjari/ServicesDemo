package com.practice.servicesdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.practice.servicesdemo.databinding.ActivityMainBinding
import com.practice.servicesdemo.services.RingtoneService

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBt.setOnClickListener(this)
        binding.stopBt.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v) {
            binding.startBt -> {
                startService(Intent(this, RingtoneService::class.java))
            }
            binding.stopBt -> {
                stopService(Intent(this, RingtoneService::class.java))
            }
        }
    }
}