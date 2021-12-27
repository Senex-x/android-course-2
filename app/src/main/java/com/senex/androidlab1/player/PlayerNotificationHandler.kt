package com.senex.androidlab1.player

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.views.activities.MainActivity

class PlayerNotificationHandler(
    private val context: Context,
) {
    init {
        context.createNotificationChannel(
            PLAYER_NOTIFICATION_CHANNEL_ID,
            PLAYER_NOTIFICATION_CHANNEL_NAME,
        )
    }

    private val playPendingIntent = getNotificationServicePendingIntent(Action.Play)
    private val pausePendingIntent = getNotificationServicePendingIntent(Action.Pause)
    private val previousPendingIntent = getNotificationServicePendingIntent(Action.Previous)
    private val nextPendingIntent = getNotificationServicePendingIntent(Action.Next)

    private var requestCounter = 0
    private fun getNotificationServicePendingIntent(actionExtra: Action): PendingIntent {
        val explicitServiceIntentWithAction = Intent(
            context,
            PlayerControlService::class.java
        ).apply {
            putExtra(Action.getIntentExtraKey(), actionExtra)
        }

        return context.createServicePendingIntent(explicitServiceIntentWithAction, requestCounter++)
    }

    private fun createPlayerNotificationBaseBuilder() = NotificationCompat
        .Builder(context, PLAYER_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_flask_primary_24)
        .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
        .setContentIntent(context.createExplicitPendingIntent<MainActivity>())
        .setOngoing(true)

    private fun createPlayerNotificationCurrentTrackBuilder(track: Track) =
        createPlayerNotificationBaseBuilder()
            .setContentTitle(track.trackName)
            .setContentText(track.artistName)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, track.coverRes))

    private fun createPlayerNotificationCurrentTrackStateBuilder(
        track: Track,
        playerState: PlayerState,
    ): NotificationCompat.Builder {
        val (iconId, pendingIntent) = when (playerState) {
            PlayerState.PAUSED ->
                Pair(R.drawable.ic_play_24, playPendingIntent)
            else ->
                Pair(R.drawable.ic_pause_24, pausePendingIntent)
        }

        return createPlayerNotificationCurrentTrackBuilder(track)
            .addAction(R.drawable.ic_skip_previous_24, null, previousPendingIntent)
            .addAction(iconId, null, pendingIntent)
            .addAction(R.drawable.ic_skip_next_24, null, nextPendingIntent)
    }

    fun setPlayerNotification(
        track: Track,
        playerState: PlayerState,
    ) {
        context.fireNotification(
            PLAYER_NOTIFICATION_ID,
            createPlayerNotificationCurrentTrackStateBuilder(
                track,
                playerState
            ).build()
        )
    }

    companion object {
        const val PLAYER_NOTIFICATION_CHANNEL_ID =
            "com.senex.androidlab.player-control-notification"
        const val PLAYER_NOTIFICATION_CHANNEL_NAME =
            "Player notification"
        const val PLAYER_NOTIFICATION_ID = 1

        private const val PLAYER_NOTIFICATION_INTENT_ACTION_STRING_KEY =
            "com.senex.androidlab.action"
    }

    enum class Action {
        Play,
        Pause,
        Next,
        Previous;

        companion object {
            fun getIntentExtraKey() = PLAYER_NOTIFICATION_INTENT_ACTION_STRING_KEY
        }
    }
}