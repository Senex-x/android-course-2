package com.senex.androidlab1.player

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.player.PlayerNotificationHandler.Action as PlayerNotificationAction

class PlayerControlService : Service() {
    private val notificationHandler: PlayerNotificationHandler by lazy {
        PlayerNotificationHandler(applicationContext)
    }

    private var currentState = PlayerState.NOT_STARTED
    private val stateSubscribersList = mutableListOf<(PlayerState) -> Unit>()
    private lateinit var mediaPlayer: MediaPlayer

    lateinit var currentTrack: Track

    override fun onBind(intent: Intent): MainBinder {
        if (currentState == PlayerState.NOT_STARTED) {
            setTrack(TrackRepository.getTrackForFirstTime())
        }
        return MainBinder()
    }

    inner class MainBinder : Binder() {
        fun getService(): PlayerControlService = this@PlayerControlService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent.getSerializableExtraAs<PlayerNotificationAction>(
            PlayerNotificationAction.getIntentExtraKey()
        )?.let {
            log("Received command from notification: $it")

            when (it) {
                PlayerNotificationAction.Play -> resume()
                PlayerNotificationAction.Pause -> pause()
                PlayerNotificationAction.Next -> next()
                PlayerNotificationAction.Previous -> previous()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setTrack(track: Track) {
        currentTrack = track
        mediaPlayer = MediaPlayer.create(
            this,
            currentTrack.trackRes
        )

        updatePlayerState(PlayerState.PAUSED)
    }

    fun resumeOrPauseIfCurrentOrPlayNew(trackId: Long) {
        if (isTrackCurrent(trackId)) {
            if (isNotPlaying) {
                resume()
            } else {
                pause()
            }
        } else {
            play(trackId)
        }
    }

    fun resumeOrPause() {
        if (isNotPlaying) {
            resume()
        } else {
            pause()
        }
    }

    fun play(track: Track) {
        stop()

        currentTrack = track
        mediaPlayer = MediaPlayer.create(
            this,
            currentTrack.trackRes
        )
        mediaPlayer.start()

        updatePlayerState(PlayerState.PLAYING)
    }

    fun play(trackId: Long) =
        play(TrackRepository.get(trackId)!!)

    fun resume() {
        mediaPlayer.start()

        updatePlayerState(PlayerState.PLAYING)
    }

    fun pause() {
        mediaPlayer.pause()

        updatePlayerState(PlayerState.PAUSED)
    }

    fun stop() {
        mediaPlayer.apply {
            stop()
            release()
        }

        updatePlayerState(PlayerState.STOPPED)
    }

    fun previous() = handleTrackUpdate(
        TrackRepository.getPrevFor(currentTrack.id)
    )

    fun next() = handleTrackUpdate(
        TrackRepository.getNextFor(currentTrack.id)
    )

    private fun handleTrackUpdate(newTrack: Track) {
        if (isPlaying) {
            play(newTrack)
        } else {
            setTrack(newTrack)
        }
    }

    fun getTrackElapsedDurationMillis() =
        mediaPlayer.currentPosition

    fun getTrackDurationMillis() =
        mediaPlayer.duration

    val isPlaying
        get() = currentState == PlayerState.PLAYING && mediaPlayer.isPlaying

    // val isNotPlaying = !isPlaying // YA OFICIALNO DOLBOEB

    val isNotPlaying
        get() = !isPlaying

    fun isTrackCurrent(track: Track) =
        track == currentTrack

    fun isTrackCurrent(trackId: Long) =
        isTrackCurrent(TrackRepository.get(trackId)!!)

    private fun updatePlayerState(newState: PlayerState) {
        log("Player state updated to: $newState")

        currentState = newState
        notifySubscribers()

        notificationHandler.setPlayerNotification(
            currentTrack,
            currentState
        )
    }

    fun subscribeForStateChange(
        callback: (PlayerState) -> Unit,
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
            callback(currentState)
        }
    }

    private fun notifySubscriber(
        callback: (PlayerState) -> Unit,
    ) {
        callback(currentState)
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelNotification(
            PlayerNotificationHandler.PLAYER_NOTIFICATION_ID
        )
        stop()
    }
}

enum class PlayerState {
    NOT_STARTED,
    PLAYING,
    PAUSED,
    STOPPED
}