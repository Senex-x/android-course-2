package com.senex.androidlab1.views.activities.main

import android.app.Activity
import android.database.ContentObserver
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.System.CONTENT_URI
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.views.activities.main.observers.VolumeObserver
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var volumeObserver: VolumeObserver
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // These two are not available before onCreate()
        volumeObserver = configureVolumeObserver()
        mediaPlayer = configureMediaPlayer()

        registerObserver(volumeObserver)

        binding.timePicker.configure()
    }

    override fun onResume() {
        super.onResume()

        val calendar = Calendar.getInstance()

        binding.timePicker.apply {
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterObserver(volumeObserver)
    }

    private fun TimePicker.configure() {
        setIs24HourView(true)

        setOnTimeChangedListener { _, _, _ ->
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }
    }

    private fun configureVolumeObserver() =
        VolumeObserver(
            context = this,
            Handler(Looper.getMainLooper())
        ) { systemVolumeLevel ->
            val volumeLevel: Float = calculateVolumeLevel(systemVolumeLevel)
            mediaPlayer.setVolume(volumeLevel, volumeLevel)
        }

    private fun Activity.registerObserver(
        observer: ContentObserver
    ): Unit = contentResolver.registerContentObserver(
        CONTENT_URI,
        true,
        observer
    )

    private fun Activity.unregisterObserver(
        observer: ContentObserver
    ): Unit = contentResolver.unregisterContentObserver(observer)

    private fun configureMediaPlayer() = MediaPlayer
        .create(this, R.raw.time_picker_tick)
        .apply {
            setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }

    private fun calculateVolumeLevel(systemVolumeLevel: Int): Float {
        val section = 1 / 14f
        val value = section * (systemVolumeLevel - 1)
        return if (value == 1f) 0.05f else 1 - value
    }
}

