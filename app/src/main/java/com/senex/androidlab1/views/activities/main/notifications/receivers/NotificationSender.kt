package com.senex.androidlab1.views.activities.main.notifications.receivers

import android.app.Notification
import android.app.NotificationManager

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import com.senex.androidlab1.utils.log

class NotificationSender : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        log("broadcast received")

    }
}