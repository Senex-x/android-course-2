package com.senex.androidlab1.views.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.models.Action
import com.senex.androidlab1.models.PlayerControlAction
import com.senex.androidlab1.player.PlayerService
import com.senex.androidlab1.utils.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //startForegroundService(Intent(this, PlayerService::class.java))

        val con = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val parcel = Parcel.obtain()

                PlayerControlAction(Action.START).writeToParcel(parcel, 0)

                val parcel2 = Parcel.obtain()
                service?.transact(
                    1,
                    parcel,
                    parcel2,
                    0
                )

                log(parcel2.toString())
                log("Connected")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                log("Disonnected")
            }

        }

        bindService(Intent(this, PlayerService::class.java), con, Context.BIND_AUTO_CREATE)
    }
}

