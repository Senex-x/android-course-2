package com.senex.androidlab1.player.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import androidx.media.app.NotificationCompat as MediaNotificationCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.views.activities.MainActivity

const val MAIN_NOTIFICATION_CHANNEL_ID = "main-notification-channel"

internal fun Context.createNotificationChannel(
    idString: String,
    name: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
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

internal fun buildNotification(context: Context): NotificationCompat.Builder {
    context.createNotificationChannel(MAIN_NOTIFICATION_CHANNEL_ID, "name")

    val previousIntent = Intent(context, PlayerControlService::class.java).apply {
        action = "PREVIOUS"
    }
    val resumeIntent = Intent(context, PlayerControlService::class.java).apply {
        action = "RESUMEORPAUSE"
    }
    val nextIntent = Intent(context, PlayerControlService::class.java).apply {
        action = "NEXT"
    }
    val previousPendingIntent = PendingIntent.getService(context, 0, previousIntent, 0)
    val resumePendingIntent = PendingIntent.getService(context, 1, resumeIntent, 0)
    val nextPendingIntent = PendingIntent.getService(context, 2, nextIntent, 0)

    return NotificationCompat.Builder(context, MAIN_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_flask_primary_24)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_skip_previous_24, "Previous", previousPendingIntent)
        .addAction(R.drawable.ic_play_24, "PauseOrResume", resumePendingIntent)
        .addAction(R.drawable.ic_skip_next_24, "Next", nextPendingIntent)
}

fun NotificationCompat.Builder.sendNotification(context: Context, trackId: Long) {
    val notificationIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).also {
            it.putExtra("TRACK", trackId)
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val track = TrackRepository.get(trackId)!!
    val cover = BitmapFactory.decodeResource(context.resources, track.coverRes)
    val build = this
        .setContentTitle(track.trackName)
        .setContentIntent(notificationIntent)
        .setContentText(track.artistName)
        .setStyle(MediaNotificationCompat.MediaStyle())
        .setLargeIcon(cover)
        .setColor(Color.BLACK)

    context.getSystemServiceAs<NotificationManager>(AppCompatActivity.NOTIFICATION_SERVICE)
        .notify(1, build.build())
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