package com.senex.androidlab1.views.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.player.PlayerState
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var musicService: PlayerControlService

    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
/*
        bindService(
            Intent(this, PlayerControlService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )*/
    }

    override fun onStop() {
        super.onStop()

        // If you want to stop the music service immediately
        // unbindService(connection)
    }
}

