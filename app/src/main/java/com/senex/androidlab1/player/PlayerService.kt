package com.senex.androidlab1.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.models.PlayerControlAction
import com.senex.androidlab1.player.notifications.createNotificationChannel
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.MainActivity
import java.io.FileDescriptor

class PlayerService : Service() {
    override fun onCreate() {
        super.onCreate()

        startService()
    }

    override fun onBind(intent: Intent): IBinder {

        return BinderImpl()
    }

    class BinderImpl: Binder() {
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            log("onTransact(): ${PlayerControlAction.create(data)}")

            return super.onTransact(code, data, reply, flags)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        createNotificationChannel(
            "CHANNEL_ID",
            "CHANNEL",
            NotificationCompat.PRIORITY_DEFAULT
        )

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, "CHANNEL_ID")
            .setContentTitle("getText(R.string.notification_title)")
            .setContentText("getText(R.string.notification_message)")
            .setSmallIcon(R.drawable.ic_flask_primary_24)
            .setContentIntent(pendingIntent)
            .setTicker("getText(R.string.ticker_text)")
            .build()

        startForeground(1, notification)
    }
}