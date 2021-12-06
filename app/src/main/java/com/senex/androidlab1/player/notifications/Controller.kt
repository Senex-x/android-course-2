package com.senex.androidlab1.player.notifications

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.views.activities.MainActivity

class Controller {
    // Given a media session and its context (usually the component containing the session)
// Create a NotificationCompat.Builder
    fun Activity.doWork(service: PlayerControlService) {


        // Get the session's metadata
        val controller = MediaSession(this, "TAG").controller
        val mediaMetadata = controller.metadata
        val description = mediaMetadata!!.description

        val builder = NotificationCompat.Builder(this, MAIN_NOTIFICATION_CHANNEL_ID).apply {
            // Add the metadata for the currently playing track
            setContentTitle(description.title)
            setContentText(description.subtitle)
            setSubText(description.description)
            setLargeIcon(description.iconBitmap)

            // Make the transport controls visible on the lockscreen
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            // Add an app icon and set its accent color
            // Be careful about the color
            setSmallIcon(R.drawable.ic_flask_primary_24)
/*
            // Add a pause button
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_pause_24,
                    "getString(R.string.pause)",

                    Intent(this@doWork, MainActivity::class.java)
                )
            )

            // Take advantage of MediaStyle features
            setStyle(MediaStyle()
                .setShowActionsInCompactView(0)

                // Add a cancel button
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
            )
        }

// Display the notification and place the service in the foreground
        startForeground(id, builder.build())
        */
        }
    }
}