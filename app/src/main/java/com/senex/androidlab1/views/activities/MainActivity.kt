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
    private lateinit var mService: PlayerControlService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            log("Connected")
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as PlayerControlService.BinderImpl
            mService = binder.getService()
            mBound = true

            mService.nextTrack()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(Intent(this, PlayerControlService::class.java), connection, Context.BIND_AUTO_CREATE)

        log("Service status: $mBound")
    }
}

