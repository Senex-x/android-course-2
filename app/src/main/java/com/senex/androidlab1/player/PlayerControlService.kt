package com.senex.androidlab1.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.log

class PlayerControlService : Service() {
    private var currentPlayerState = PlayerState.NOT_STARTED
    private val stateSubscribersList = mutableListOf<(PlayerState) -> Unit>()
    private lateinit var mediaPlayer: MediaPlayer
    private val binder = MainBinder()
    lateinit var currentTrack: Track

    fun play(track: Track) {
        log("play()")
        currentTrack = track
        mediaPlayer = MediaPlayer.create(
            this,
            currentTrack.trackRes
        )
        mediaPlayer.start()

        currentPlayerState = PlayerState.PLAYING
        notifySubscribers()
    }

    val isPlaying
        get() = currentPlayerState == PlayerState.PLAYING

    fun resume() {
        mediaPlayer.start()

        currentPlayerState = PlayerState.PLAYING
        notifySubscribers()
    }

    fun pause() {
        mediaPlayer.pause()

        currentPlayerState = PlayerState.PAUSED
        notifySubscribers()
    }

    fun stop() {
        log("stop()")
        mediaPlayer.apply {
            stop()
            release()
        }

        currentPlayerState = PlayerState.STOPPED
        notifySubscribers()
    }

    fun subscribeForStateChange(callback: (PlayerState) -> Unit) {
        stateSubscribersList.add(callback)
        notifySubscriber(callback)
    }

    fun unsubscribeForStateChange(callbackToInvalidate: (PlayerState) -> Unit) {
        stateSubscribersList.removeIf { callback -> callback == callbackToInvalidate }
    }

    private fun notifySubscribers() {
        for (callback in stateSubscribersList) {
            callback(currentPlayerState)
        }
    }

    private fun notifySubscriber(callback: (PlayerState) -> Unit) {
        callback(currentPlayerState)
    }

    override fun onDestroy() {
        super.onDestroy()

        stop()
    }

    fun getState() = currentPlayerState

    override fun onBind(intent: Intent) = binder

    inner class MainBinder : Binder() {
        fun getService(): PlayerControlService = this@PlayerControlService
    }
}

enum class PlayerState {
    NOT_STARTED,
    PLAYING,
    PAUSED,
    STOPPED
}