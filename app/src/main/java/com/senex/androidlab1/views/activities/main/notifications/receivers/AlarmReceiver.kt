package com.senex.androidlab1.views.activities.main.notifications.receivers

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.senex.androidlab1.database.MainDatabase
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.main.notifications.*
import com.senex.androidlab1.views.activities.main.notifications.fireNotification
import com.senex.androidlab1.views.activities.main.notifications.rtcToTime
import com.senex.androidlab1.views.activities.main.notifications.wakeUpScreen

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = intent.getParcelableExtra<Notification>("notification")
        val alarmDao = MainDatabase.instance.alarmDao()
        val allAlerts = alarmDao.getAll()

        if (notification != null && allAlerts.isNotEmpty()) {
            val id = allAlerts[0].notificationId

            context.fireNotification(id, notification)
            context.wakeUpScreen()
            context.disableBootReceiver()
            alarmDao.deleteByKey(id)

            val timePair = rtcToTime(System.currentTimeMillis())
            log("Alarm received at " +
                    "time: ${prettyPrintTime(timePair.first, timePair.second)}, " +
                    "RTC: ${System.currentTimeMillis()}")
        }
    }
}