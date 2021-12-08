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
    lateinit var currentTrack: Track

    fun play(track: Track) {
        log("play()")
        currentTrack = track
        mediaPlayer = MediaPlayer.create(
            this,
            currentTrack.trackRes
        )
        mediaPlayer.start()

        updatePlayerState(PlayerState.PLAYING)
    }

    fun resume() {
        mediaPlayer.start()

        updatePlayerState(PlayerState.PLAYING)
    }

    fun pause() {
        mediaPlayer.pause()

        updatePlayerState(PlayerState.PAUSED)
    }

    fun stop() {
        log("stop()")
        mediaPlayer.apply {
            stop()
            release()
        }

        updatePlayerState(PlayerState.STOPPED)
    }

    val isPlaying
        get() = currentPlayerState == PlayerState.PLAYING

    private fun updatePlayerState(newState: PlayerState) {
        currentPlayerState = newState
        notifySubscribers()
    }

    fun subscribeForStateChange(
        callback: (PlayerState) -> Unit
    ) {
        stateSubscribersList.add(callback)
        notifySubscriber(callback)
    }

    fun unsubscribeFromStateChange(
        callbackToUnsubscribe: (PlayerState) -> Unit,
    ) = stateSubscribersList.removeIf { callback ->
        callback == callbackToUnsubscribe
    }

    private fun notifySubscribers() {
        for (callback in stateSubscribersList) {
            callback(currentPlayerState)
        }
    }

    private fun notifySubscriber(callback: (PlayerState) -> Unit) {
        callback(currentPlayerState)
    }

    fun isTrackCurrent(track: Track) =
        currentPlayerState != PlayerState.NOT_STARTED && track == currentTrack

    override fun onBind(intent: Intent) = MainBinder()

    inner class MainBinder : Binder() {
        fun getService(): PlayerControlService = this@PlayerControlService
    }

    override fun onDestroy() {
        super.onDestroy()

        stop()
    }
}

enum class PlayerState {
    NOT_STARTED,
    PLAYING,
    PAUSED,
    STOPPED
}