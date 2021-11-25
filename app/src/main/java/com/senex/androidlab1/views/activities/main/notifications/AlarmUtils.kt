package com.senex.androidlab1.views.activities.main.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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

internal fun Context.enableBootReceiver() =
    setReceiverEnabledState<BootReceiver>(
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    )

internal fun Context.disableBootReceiver() =
    setReceiverEnabledState<BootReceiver>(
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    )

private inline fun <reified T> Context.setReceiverEnabledState(stateCode: Int) {
    val receiver = ComponentName(this, T::class.java)

    packageManager.setComponentEnabledSetting(
        receiver,
        stateCode,
        PackageManager.DONT_KILL_APP
    )
}

internal fun Context.setAlarmForRtc(hour: Int, minute: Int) {
    setAlarmForRtc(timeToRtc(System.currentTimeMillis(), hour, minute))
}

internal fun Context.setAlarmForRtc(rtcTime: Long) {
    val alarmManager = getSystemServiceAs<AlarmManager>(Context.ALARM_SERVICE)

    val notification = buildNotification(
        channelId = ALERT_CHANNEL_ID,
        title = "Title",
        content = "Content",
        onClickIntent = this.createImplicitPendingIntent<WakeActivity>(),
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

    enableBootReceiver()
}

internal fun timeToRtc(dayRtc: Long, hour: Int, minute: Int): Long =
    Calendar.getInstance().run {
        time = Date(dayRtc)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        time.time
    }

internal fun rtcToTime(rtc: Long): Pair<Int, Int> {
    val dateString = (SimpleDateFormat("HH:mm", Locale.US))
        .format(Date(rtc))

    return Pair(
        dateString.substring(0, 2).toInt(),
        dateString.substring(3).toInt()
    )
}

internal inline fun <reified T> Context.cancelAlarmForReceiver() {
    val intent = Intent(this, T::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE)
    val alarmManager = getSystemServiceAs<AlarmManager>(Context.ALARM_SERVICE)
    if (pendingIntent != null) {
        alarmManager.cancel(pendingIntent)
    }
    log("Alarm has been cancelled")
}

internal fun prettyPrintTime(hour: Int, minute: Int): String {
    val hourString = if (hour / 10 == 0) "0$hour" else hour.toString()
    val minuteString = if (minute / 10 == 0) "0$minute" else minute.toString()
    return "$hourString:$minuteString"
}
