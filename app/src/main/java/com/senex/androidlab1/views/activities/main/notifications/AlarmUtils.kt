package com.senex.androidlab1.views.activities.main.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.senex.androidlab1.database.MainDatabase
import com.senex.androidlab1.model.Alarm
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.main.ALERT_CHANNEL_ID
import com.senex.androidlab1.views.activities.main.notifications.receivers.AlarmReceiver
import com.senex.androidlab1.views.activities.main.notifications.receivers.BootReceiver
import com.senex.androidlab1.views.activities.wake.WakeActivity
import java.text.SimpleDateFormat
import java.util.*


internal inline fun <reified T> Context.enableBootReceiver() {
    setReceiverEnabledState<T>(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
}

internal inline fun <reified T> Context.disableBootReceiver() {
    setReceiverEnabledState<T>(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
}

private inline fun <reified T> Context.setReceiverEnabledState(stateCode: Int) {
    val receiver = ComponentName(this, T::class.java)

    packageManager.setComponentEnabledSetting(
        receiver,
        stateCode,
        PackageManager.DONT_KILL_APP
    )
}

internal fun Context.setAlarmUtc(hour: Int, minute: Int) {
    setAlarmUtc(timeToRtc(System.currentTimeMillis(), hour, minute))
}

internal fun Context.setAlarmUtc(rtcTime: Long) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notification = buildNotification(
        channelId = ALERT_CHANNEL_ID,
        title = "Title",
        content = "Content",
        onClickIntent = createImplicitPendingIntent<WakeActivity>(),
    )

    val newNotificationId = Random().nextInt()

    val pendingIntent = createImplicitBroadcastIntent<AlarmReceiver>(
        newNotificationId,
        notification
    )


    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        rtcTime,
        pendingIntent
    )

    val (hour, minute) = rtcToTime(rtcTime)

    log("Setting alarm for: $hour:$minute, RTC: $rtcTime")

    MainDatabase.instance.alarmDao().insert(Alarm(
        notificationId = newNotificationId,
        hour = hour,
        minute = minute,
        rtcTime = rtcTime,
    ))

    enableBootReceiver<BootReceiver>()
}

internal fun timeToRtc(currentRtc: Long, hour: Int, minute: Int): Long =
    Calendar.getInstance().run {
        time = Date(currentRtc)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        time.time
    }


internal fun rtcToTime(rtc: Long): Pair<Int, Int> {
    val dateString = (SimpleDateFormat("HH:mm", Locale.US)).format(Date(rtc))

    return Pair(
        dateString.substring(0, 2).toInt(),
        dateString.substring(3).toInt()
    )
}

internal fun Context.cancelAlarm(intent: PendingIntent): Unit =
    (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        .cancel(intent)
