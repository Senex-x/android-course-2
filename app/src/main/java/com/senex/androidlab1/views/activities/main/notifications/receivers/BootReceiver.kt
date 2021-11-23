package com.senex.androidlab1.views.activities.main.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.senex.androidlab1.utils.log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            log("RECEIVED")
        }
    }
}