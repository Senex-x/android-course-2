package com.senex.androidlab1.views.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.utils.log

class MainActivity : AppCompatActivity() {
    private lateinit var controlService: PlayerControlService
    private var isServiceBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder,
        ) {
            log("Connected")

            val binder = service as PlayerControlService.MainBinder
            controlService = binder.getService()
            isServiceBound = true
        }

        override fun onServiceDisconnected(
            arg0: ComponentName
        ) {
            isServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()

        bindService(
            Intent(this, PlayerControlService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()

        unbindService(connection)
        isServiceBound = false
    }
}

