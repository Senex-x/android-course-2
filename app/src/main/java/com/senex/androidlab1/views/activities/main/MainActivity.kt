package com.senex.androidlab1.views.activities.main


import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.System.CONTENT_URI
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.views.activities.main.observers.VolumeObserver
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var volumeObserver: VolumeObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volumeObserver = VolumeObserver(
            context = this,
            Handler(Looper.getMainLooper())
        )

        contentResolver.registerContentObserver(
            CONTENT_URI,
            true,
            volumeObserver
        )

        binding.timePicker.apply {
            setIs24HourView(true)

            val mediaPlayer = MediaPlayer.create(context, R.raw.time_picker_tick)
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnTimeChangedListener { timePicker, i, i1 ->
                val systemVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                val volumeLevel: Float = calculateVolumeLevel(systemVolumeLevel)
                mediaPlayer.setVolume(volumeLevel, volumeLevel) //set volume takes two parameters
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }
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

        contentResolver.unregisterContentObserver(volumeObserver)
    }

    private fun calculateVolumeLevel(systemVolumeLevel: Int): Float {
        val section = 1 / 14f
        val value = section * (systemVolumeLevel - 1)
        return if (value == 1f) 0.05f else 1 - value
    }
}

