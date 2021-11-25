package com.senex.androidlab1.views.activities.main

import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.database.MainDatabase
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.views.activities.main.notifications.*
import com.senex.androidlab1.views.activities.main.notifications.receivers.AlarmReceiver
import com.senex.androidlab1.views.activities.main.observers.configureVolumeObserver
import com.senex.androidlab1.views.activities.main.observers.registerObserver
import com.senex.androidlab1.views.activities.main.observers.unregisterObserver
import com.senex.androidlab1.views.activities.main.observers.volume.VolumeObserver
import java.util.*

internal const val ALERT_CHANNEL_ID = "ALERT_CHANNEL_ID"
internal const val ALERT_CHANNEL_NAME = "ALERT_CHANNEL"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var volumeObserver: VolumeObserver
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainDatabase.init(this)
        log("Database state snapshot: " +
                MainDatabase.instance.alarmDao().getAll().toString())
        // If an alarm should have been triggered while a device was turned off
        MainDatabase.instance.alarmDao().deleteAllOutdated(System.currentTimeMillis())
        // For debug
        //MainDatabase.instance.alarmDao().deleteAll()

        // These two are not available before onCreate()
        mediaPlayer = configureMediaPlayer()
        volumeObserver = configureVolumeObserver(mediaPlayer)

        registerObserver(volumeObserver)

        createNotificationChannel(
            ALERT_CHANNEL_ID,
            ALERT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        configureTimePicker()
        configureButtonSet(binding.timePicker)
        configureButtonCancel()
    }

    private fun initAlarmStatus() {
        val allAlerts = MainDatabase.instance.alarmDao().getAll()

        if (allAlerts.isNotEmpty()) {
            val alert = allAlerts[0]
            setAlarmStatus(alert.hour, alert.minute)
        } else {
            setAlarmStatusDefault()
        }
    }

    private fun configureTimePicker() {
        binding.timePicker.run {
            setIs24HourView(true)

            setOnTimeChangedListener { _, _, _ ->
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }
    }

    private fun configureButtonSet(timePicker: TimePicker): Unit =
        binding.buttonSet.setOnClickListener {
            cancelCurrentAlarm()

            val hour = timePicker.hour
            val minute = timePicker.minute

            setAlarmForRtc(hour, minute)
            setAlarmStatus(hour, minute)
        }

    private fun configureButtonCancel(): Unit =
        binding.buttonCancel.setOnClickListener {
            cancelCurrentAlarm()
        }

    private fun cancelCurrentAlarm() {
        val allAlarms = MainDatabase.instance.alarmDao().getAll()
        if (allAlarms.isNotEmpty()) {
            handleAlarmCancelForReceiver<AlarmReceiver>(
                allAlarms[0].notificationId
            )
        }
    }

    private inline fun <reified T> handleAlarmCancelForReceiver(notificationId: Int) {
        cancelNotification(notificationId)
        MainDatabase.instance.alarmDao().deleteByKey(notificationId)
        cancelAlarmForReceiver<T>()
        setAlarmStatusDefault()
    }

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

    private fun setAlarmStatusDefault() {
        binding.alarmStatus.text =
            getString(R.string.message_alarm_is_not_set)
    }

    private fun setAlarmStatus(hour: Int, minute: Int) {
        binding.alarmStatus.text =
            getString(
                R.string.message_alarm_status,
                prettyPrintTime(hour, minute)
            )
    }

    override fun onResume() {
        super.onResume()

        val calendar = Calendar.getInstance()

        binding.timePicker.apply {
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
        }

        initAlarmStatus()
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterObserver(volumeObserver)
    }
}



