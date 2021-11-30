package com.senex.androidlab1.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.player.notifications.createNotificationChannel
import com.senex.androidlab1.views.activities.MainActivity
import java.io.FileDescriptor

class PlayerService : Service() {
    override fun onCreate() {
        super.onCreate()

        startService()
    }

    override fun onBind(intent: Intent): IBinder {
        return object : IBinder {
            override fun getInterfaceDescriptor(): String? {
                TODO("Not yet implemented")
            }

            override fun pingBinder(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isBinderAlive(): Boolean {
                TODO("Not yet implemented")
            }

            override fun queryLocalInterface(descriptor: String): IInterface? {
                TODO("Not yet implemented")
            }

            override fun dump(fd: FileDescriptor, args: Array<out String>?) {
                TODO("Not yet implemented")
            }

            override fun dumpAsync(fd: FileDescriptor, args: Array<out String>?) {
                TODO("Not yet implemented")
            }

            override fun transact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
                TODO("Not yet implemented")
            }

            override fun linkToDeath(recipient: IBinder.DeathRecipient, flags: Int) {
                TODO("Not yet implemented")
            }

            override fun unlinkToDeath(recipient: IBinder.DeathRecipient, flags: Int): Boolean {
                TODO("Not yet implemented")
            }

        }
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