package com.senex.androidlab1.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

internal fun Context.createNotificationChannel(
    id: String,
    name: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
    visibility: Int = Notification.VISIBILITY_PUBLIC,
) {
    val channel = NotificationChannel(
        id,
        name,
        importance
    ).apply {
        lockscreenVisibility = visibility
        setSound(null, null)
    }

    getSystemServiceAs<NotificationManager>()
        .createNotificationChannel(channel)
}

internal fun Context.createServicePendingIntent(
    intent: Intent,
    requestCode: Int = 0,
    flag: Int = PendingIntent.FLAG_CANCEL_CURRENT,
): PendingIntent = PendingIntent.getService(
    this,
    requestCode,
    intent,
    flag
)

internal fun Context.createActivityPendingIntent(
    intent: Intent,
    requestCode: Int = 0,
    flag: Int = PendingIntent.FLAG_CANCEL_CURRENT,
): PendingIntent = PendingIntent.getActivity(
    this,
    requestCode,
    intent,
    flag
)

internal inline fun <reified T> Context.createExplicitPendingIntent(
    requestCode: Int = 0,
    flag: Int = PendingIntent.FLAG_CANCEL_CURRENT,
) = createActivityPendingIntent(
    Intent(this, T::class.java),
    requestCode,
    flag
)

internal inline fun <reified T> Context.createExplicitBroadcastIntent(
    id: Int,
    notification: Notification,
): PendingIntent {
    val intent = Intent(this, T::class.java)
    intent.putExtra("notification_id", id)
    intent.putExtra("notification", notification)
    return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

internal fun Context.fireNotification(
    id: Int,
    notification: Notification,
): Unit = NotificationManagerCompat
    .from(this)
    .notify(id, notification)

internal fun Context.cancelNotification(
    id: Int,
): Unit = getSystemServiceAs<NotificationManager>(AppCompatActivity.NOTIFICATION_SERVICE)
    .cancel(id)

internal fun Context.wakeUpScreen() {
    val powerManager = getSystemServiceAs<PowerManager>(Context.POWER_SERVICE)

    if (!powerManager.isInteractive) {
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "senex.androidlab:notification-lock"
        )
        wakeLock.acquire(3000)
    }
}

internal inline fun <reified T> Context.getSystemServiceAs(name: String) =
    getSystemService(name) as T

internal inline fun <reified T> Context.getSystemServiceAs(): T {
    val serviceId = when (T::class.java.canonicalName) {
        NotificationManager::class.java.canonicalName -> AppCompatActivity.NOTIFICATION_SERVICE
        PowerManager::class.java.canonicalName -> Context.POWER_SERVICE
        else -> throw IllegalArgumentException()
    }

    return getSystemServiceAs(serviceId)
}

internal inline fun <reified T> Intent?.getSerializableExtraAs(key: String) =
    this?.getSerializableExtra(key) as? T
