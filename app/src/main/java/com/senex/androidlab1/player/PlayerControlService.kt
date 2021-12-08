package com.senex.androidlab1.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Parcel
import com.senex.androidlab1.models.PlayerControlAction
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.log

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
}