package com.senex.androidlab1.player

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Parcel
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.models.PlayerControlAction
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.player.notifications.MAIN_NOTIFICATION_CHANNEL_ID
import com.senex.androidlab1.player.notifications.createNotificationChannel
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.MainActivity

class PlayerControlService : Service() {
    lateinit var mediaPlayer: MediaPlayer
    private val binder = MainBinder()
    var currentTrackId: Long? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()
        //startService()
    }

    fun nextTrack() {
        log("Next track service command")
    }

    override fun onBind(intent: Intent) = binder

    inner class MainBinder : Binder() {
        fun getService(): PlayerControlService = this@PlayerControlService

        override fun onTransact(
            code: Int,
            data: Parcel,
            reply: Parcel?,
            flags: Int,
        ): Boolean {
            val playerControlAction = PlayerControlAction.create(data)

            playerControlAction.writeToParcel(reply!!, 0)

            return super.onTransact(code, data, reply, flags)
        }
    }

    fun play(track: Track) {
        mediaPlayer = MediaPlayer.create(
            this,
            track.trackRes
        ).also {
            it.start()
        }
    }

    fun pauseOrResume(): Int {
        return if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            1
        } else {
            mediaPlayer.start()
            0
        }
    }

    fun stop() {
        with(mediaPlayer) {
            stop()
            release()
        }
    }

    private fun startService() {
        createNotificationChannel(
            MAIN_NOTIFICATION_CHANNEL_ID,
            "CHANNEL",
            NotificationCompat.PRIORITY_DEFAULT
        )

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, "CHANNEL_ID")
            .setContentTitle("getText(R.string.notification_title)")
            .setContentText("getText(R.string.notification_message)")
            .setSmallIcon(R.drawable.ic_flask_primary_24)
            .setContentIntent(pendingIntent)
            .setTicker("getText(R.string.ticker_text)")
            .build()

        startForeground(1, notification)
    }
}