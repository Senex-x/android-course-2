package com.senex.androidlab1.views.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.player.PlayerState
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var musicService: PlayerControlService

    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicService = (service as PlayerControlService.MainBinder).getService()

            musicService.subscribeForStateChange(stateListener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // TODO: Inspect option
        }
    }

    private val stateListener: (PlayerState) -> Unit = {
        log("Music player state has changed to: $it")

        binding.displayPlayerState(it)
    }

    private fun ActivityMainBinding.displayPlayerState(state: PlayerState) {
        val icon = if (state == PlayerState.PLAYING)
            R.drawable.ic_pause_24 else R.drawable.ic_play_24

        playPauseButton.icon = setThemedIcon(icon)
    }

    private fun setThemedIcon(@DrawableRes id: Int) =
        ContextCompat.getDrawable(
            this,
            id
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            initPlayPauseButton()
            initPrevButton()
            initNextButton()
        }
    }

    private fun ActivityMainBinding.initPlayPauseButton() {
        playPauseButton.setOnClickListener {
            musicService.run {
                if (isPlaying) {
                    pause()
                } else {
                    resume()
                }
            }
        }
    }

    private fun ActivityMainBinding.initNextButton() {
        nextButton.setOnClickListener {
            musicService.next()
        }
    }

    private fun ActivityMainBinding.initPrevButton() {
        previousButton.setOnClickListener {
            musicService.previous()
        }
    }

    override fun onStart() {
        super.onStart()

        bindService(
            Intent(this, PlayerControlService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()

        musicService.unsubscribeFromStateChange(stateListener)
        // If you want to stop the music service immediately
        // unbindService(connection)
    }
}

