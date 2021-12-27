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
    private lateinit var currentTrack: Track

    override fun onBind(intent: Intent): MainBinder {
        // TODO: Inspect NOT_STARTED checks
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
        log("PlayerService set()")
        currentTrack = track
        mediaPlayer = MediaPlayer.create(
            this,
            currentTrack.trackRes
        )

        updatePlayerState(PlayerState.PAUSED)
    }

    fun pauseResumeIfCurrentOrPlayNew(trackId: Long) {
        if (isTrackCurrent(trackId)) {
            if (isPlaying) {
                pause()
            } else {
                resume()
            }
        } else {
            stop()
            play(trackId)
        }
    }

    fun play(track: Track) {
        log("PlayerService play()")
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
        log("PlayerService resume()")
        mediaPlayer.start()


        updatePlayerState(PlayerState.PLAYING)
    }

    fun pause() {
        log("PlayerService pause()")
        mediaPlayer.pause()

        updatePlayerState(PlayerState.PAUSED)
    }

    fun stop() {
        log("PlayerService stop()")
        mediaPlayer.apply {
            stop()
            release()
        }

        updatePlayerState(PlayerState.STOPPED)
    }

    fun previous() {
        if (isInitialized) {
            handleTrackUpdate(
                TrackRepository.getPrevFor(currentTrack.id)
            )
        } else {
            setTrack(TrackRepository.getTrackForFirstTime())
        }
    }

    fun next() {
        if (isInitialized) {
            handleTrackUpdate(
                TrackRepository.getNextFor(currentTrack.id)
            )
        } else {
            setTrack(TrackRepository.getTrackForFirstTime())
        }
    }

    private fun handleTrackUpdate(newTrack: Track) {
        if (isPlaying) {
            stop()
            play(newTrack)
        } else {
            setTrack(newTrack)
        }
    }

    private val isInitialized
        get() = currentState != PlayerState.NOT_STARTED

    val isPlaying
        get() = currentState == PlayerState.PLAYING

    private fun updatePlayerState(newState: PlayerState) {
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

    fun isTrackCurrent(track: Track) =
        currentState != PlayerState.NOT_STARTED
                && track == currentTrack

    fun isTrackCurrent(trackId: Long) =
        isTrackCurrent(TrackRepository.get(trackId)!!)

    override fun onDestroy() {
        super.onDestroy()

        cancelNotification(PlayerNotificationHandler.PLAYER_NOTIFICATION_ID)
        stop()
    }
}

enum class PlayerState {
    NOT_STARTED,
    PLAYING,
    PAUSED,
    STOPPED
}