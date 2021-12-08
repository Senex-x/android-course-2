package com.senex.androidlab1.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.log

class PlayerControlService : Service() {
    private var currentPlayerState = PlayerState.CREATED
    private var isInitialized = false
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

        isInitialized = true
        currentPlayerState = PlayerState.PLAYING
        notifySubscribers()
    }

    val isPlaying
        get() = isInitialized && mediaPlayer.isPlaying

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

        isInitialized = false
        currentPlayerState = PlayerState.STOPPED
        notifySubscribers()
    }

    fun subscribeForStateChange (callback: (PlayerState) -> Unit) {
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

    override fun onBind(intent: Intent) = binder

    inner class MainBinder : Binder() {
        fun getService(): PlayerControlService = this@PlayerControlService
    }
}

enum class PlayerState {
    CREATED,
    PLAYING,
    PAUSED,
    STOPPED
}