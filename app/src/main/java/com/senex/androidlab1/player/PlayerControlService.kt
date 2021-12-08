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
    lateinit var currentTrack: Track

    fun play(track: Track) {
        log("play()")
        currentTrack = track
        mediaPlayer = MediaPlayer.create(
            this,
            currentTrack.trackRes
        ).apply {
            start()
        }
    }

    private fun pauseOrResume(): Int {
        log("pauseOrResume()")
        return if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            1
        } else {
            mediaPlayer.start()
            0
        }
    }

    fun stop() {
        log("stop()")
        mediaPlayer.apply {
            stop()
            release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stop()
    }

    override fun onBind(intent: Intent) = binder

    inner class MainBinder : Binder() {
        fun getService(): PlayerControlService = this@PlayerControlService
    }
}