package com.senex.androidlab1.views.activities.main.notifications.receivers

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.main.notifications.fireNotification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        log("Alarm received")

        val notification = intent.getParcelableExtra<Notification>("notification")

        if (notification != null) {
            context.fireNotification(1, notification)
        }
    }
}