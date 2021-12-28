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

    private lateinit var mediaPlayer: MediaPlayer
    private var currentState = State.NOT_STARTED
    private val stateSubscribersList = mutableListOf<(State) -> Unit>()

    lateinit var currentTrack: Track

    override fun onBind(intent: Intent): MainBinder {
        if (currentState == State.NOT_STARTED) {
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

        updatePlayerState(State.PAUSED)
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

        updatePlayerState(State.PLAYING)
    }

    fun play(trackId: Long) =
        play(TrackRepository.get(trackId)!!)

    fun resume() {
        mediaPlayer.start()

        updatePlayerState(State.PLAYING)
    }

    fun pause() {
        mediaPlayer.pause()

        updatePlayerState(State.PAUSED)
    }

    fun stop() {
        mediaPlayer.apply {
            stop()
            release()
        }

        updatePlayerState(State.STOPPED)
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

    val isPlaying
        get() = currentState == State.PLAYING
                && mediaPlayer.isPlaying

    val isNotPlaying
        get() = !isPlaying

    private fun isTrackCurrent(track: Track) =
        track == currentTrack

    private fun isTrackCurrent(trackId: Long) =
        isTrackCurrent(TrackRepository.get(trackId)!!)

    private fun updatePlayerState(newState: State) {
        log("Player state updated to: $newState")

        currentState = newState
        notifySubscribers()

        notificationHandler.setPlayerNotification(
            currentTrack,
            currentState
        )
    }

    fun subscribeForStateChange(
        callback: (State) -> Unit,
    ) {
        stateSubscribersList.add(callback)
        callback(currentState)
    }

    fun unsubscribeFromStateChange(
        callbackToUnsubscribe: (State) -> Unit,
    ) = stateSubscribersList.removeIf { callback ->
        callback == callbackToUnsubscribe
    }

    private fun notifySubscribers() {
        for (callback in stateSubscribersList) {
            callback(currentState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelNotification(
            PlayerNotificationHandler.PLAYER_NOTIFICATION_ID
        )
        stop()
    }

    enum class State {
        NOT_STARTED,
        PLAYING,
        PAUSED,
        STOPPED
    }
}

