package com.senex.androidlab1.player.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.R

internal fun Context.createNotificationChannel(
    idString: String,
    name: String,
    importance: Int,
) {
    val channel = NotificationChannel(idString, name, importance).apply {
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        vibrationPattern = longArrayOf(0, 1000) // wait before start, keep enabled
        setSound(
            Uri.parse("android.resource://$packageName/${R.raw.zeromancer_dr_online}"),
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
        )
    }

    getSystemServiceAs<NotificationManager>(AppCompatActivity.NOTIFICATION_SERVICE)
        .createNotificationChannel(channel)
}

internal fun Context.buildNotification(
    channelId: String,
    title: String,
    content: String,
    onClickIntent: PendingIntent?,
) = NotificationCompat
    .Builder(this, channelId)
    .setSmallIcon(R.drawable.ic_flask_primary_24)
    .setContentTitle(title)
    .setContentText(content)
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    .setAutoCancel(true)
    .setContentIntent(onClickIntent)
    .build()

internal inline fun <reified T> Context.getSystemServiceAs(name: String) =
    getSystemService(name) as T