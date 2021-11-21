package com.senex.androidlab1.views.activities.main.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.senex.androidlab1.R

internal fun Context.createNotificationChannel(
    idString: String,
    name: String,
    importance: Int,
) {
    val channel = NotificationChannel(idString, name, importance)
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

internal inline fun <reified T> Context.createPendingIntentFor(): PendingIntent {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    return PendingIntent.getActivity(this, 0, intent, 0)
}

internal fun Context.buildNotification(
    channelId: String,
    title: String,
    content: String,
    priority: Int,
    onClickIntent: PendingIntent?,
) = NotificationCompat
    .Builder(this, channelId)
    .setSmallIcon(R.drawable.flask_icon)
    .setContentTitle(title)
    .setStyle(
        NotificationCompat
            .BigTextStyle()
            .bigText(content)
    )
    .setPriority(priority)
    .setContentIntent(onClickIntent)
    .setAutoCancel(true)
    .build()

internal fun Context.fireNotification(
    id: Int,
    notification: Notification,
): Unit = NotificationManagerCompat
    .from(this)
    .notify(id, notification)

internal fun Context.cancelNotification(
    id: Int?
): Unit? = id?.let {
    (getSystemService(AppCompatActivity.NOTIFICATION_SERVICE)
                    as NotificationManager)
        .cancel(it)
}





