package com.senex.androidlab1.views.activities.main

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.System.CONTENT_URI
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.button.MaterialButton
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.utils.toast
import com.senex.androidlab1.views.activities.main.observers.VolumeObserver
import com.senex.androidlab1.views.activities.wake.WakeActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var volumeObserver: VolumeObserver
    private lateinit var mediaPlayer: MediaPlayer

    private var currentNotificationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // These two are not available before onCreate()
        volumeObserver = configureVolumeObserver()
        mediaPlayer = configureMediaPlayer()

        registerObserver(volumeObserver)

        createNotificationChannel()

        binding.run {
            timePicker.configureButtonSet()
            buttonSet.configureButtonSet(timePicker)
            buttonCancel.configureButtonCancel()
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

        unregisterObserver(volumeObserver)
    }

    private fun TimePicker.configureButtonSet() {
        setIs24HourView(true)

        setOnTimeChangedListener { _, _, _ ->
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }
    }

    private fun MaterialButton.configureButtonSet(timePicker: TimePicker) {
        setOnClickListener {
            val minute = timePicker.minute
            val hour = timePicker.hour

            val intent = Intent(this@MainActivity, WakeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(this@MainActivity, 0, intent, 0)

            val builder = NotificationCompat
                .Builder(this@MainActivity, "CHANNEL_ID")
                .setSmallIcon(R.drawable.flask_icon)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText("Much longer text that cannot fit one line in a notification")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            currentNotificationId = Random().nextInt()

            NotificationManagerCompat
                .from(this@MainActivity)
                .notify(currentNotificationId!!, builder.build())

            toast("Notification set for time $hour:$minute")
        }
    }

    private fun MaterialButton.configureButtonCancel() {
        setOnClickListener {
            cancelNotification(currentNotificationId)
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_notifications)
        val descriptionText = "R.string.channel_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
        // Register the channel with the system
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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
        .create(this, R.raw.tick_2)
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

    private fun Context.cancelNotification(notificationId: Int?): Unit =
        notificationId?.let {
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .cancel(it)
        } ?: Unit

}



