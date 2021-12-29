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

internal class NotificationHandler(
    private val context: Context,
) {
    internal fun createNotificationChannel(
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

    internal fun createServicePendingIntent(
        intent: Intent,
        requestCode: Int = 0,
        flag: Int = PendingIntent.FLAG_CANCEL_CURRENT,
    ): PendingIntent = PendingIntent.getService(
        context,
        requestCode,
        intent,
        flag
    )

    internal fun createActivityPendingIntent(
        intent: Intent,
        requestCode: Int = 0,
        flag: Int = PendingIntent.FLAG_CANCEL_CURRENT,
    ): PendingIntent = PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        flag
    )

    internal inline fun <reified T> createExplicitPendingIntent(
        requestCode: Int = 0,
        flag: Int = PendingIntent.FLAG_CANCEL_CURRENT,
    ) = createActivityPendingIntent(
        Intent(context, T::class.java),
        requestCode,
        flag
    )

    internal fun fireNotification(
        id: Int,
        notification: Notification,
    ): Unit = NotificationManagerCompat
        .from(context)
        .notify(id, notification)

    internal fun cancelNotification(
        id: Int,
    ): Unit = getSystemServiceAs<NotificationManager>(AppCompatActivity.NOTIFICATION_SERVICE)
        .cancel(id)

    internal fun wakeUpScreen() {
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

    internal inline fun <reified T> getSystemServiceAs(name: String) =
        context.getSystemService(name) as T

    internal inline fun <reified T> getSystemServiceAs(): T {

        val serviceId = when (T::class.java.canonicalName) {
            NotificationManager::class.java.canonicalName ->
                AppCompatActivity.NOTIFICATION_SERVICE
            PowerManager::class.java.canonicalName ->
                Context.POWER_SERVICE
            else -> throw IllegalArgumentException()
        }

        return getSystemServiceAs(serviceId)
    }
}