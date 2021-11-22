package com.senex.androidlab1.views.activities.main.notifications

import android.app.Notification
import android.app.NotificationManager

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import com.senex.androidlab1.utils.log

class NotificationSender : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        log("broadcast received")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification? = intent.getParcelableExtra(NOTIFICATION)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }
}