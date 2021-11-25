package com.senex.androidlab1.views.activities.main.notifications.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.senex.androidlab1.database.MainDatabase
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.main.notifications.setAlarmForRtc

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarmDao = MainDatabase.instance.alarmDao()
            val allAlarms = alarmDao.getAll()

            if (allAlarms.isNotEmpty()) {
                log("Setting alarm from saved state after reboot.")
                val alarm = allAlarms[0]
                context.setAlarmForRtc(alarm.rtcTime)
                alarmDao.deleteByKey(alarm.notificationId)
            }
        }
    }
}