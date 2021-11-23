package com.senex.androidlab1.views.activities.main

import android.app.*
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ActivityMainBinding
import com.senex.androidlab1.views.activities.main.notifications.*
import com.senex.androidlab1.views.activities.main.notifications.createNotificationChannel
import com.senex.androidlab1.views.activities.main.observers.configureVolumeObserver
import com.senex.androidlab1.views.activities.main.observers.registerObserver
import com.senex.androidlab1.views.activities.main.observers.unregisterObserver
import com.senex.androidlab1.views.activities.main.observers.volume.VolumeObserver
import java.util.*

import com.senex.androidlab1.utils.log

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.senex.androidlab1.views.activities.main.notifications.receivers.AlarmReceiver
import com.senex.androidlab1.views.activities.wake.WakeActivity

private const val NOTIFICATION_CHANNEL_ID = "MAIN_CHANNEL_ID"
private const val NOTIFICATION_CHANNEL_NAME = "MAIN_CHANNEL"

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
        mediaPlayer = configureMediaPlayer()
        volumeObserver = configureVolumeObserver(mediaPlayer)

        registerObserver(volumeObserver)

        createNotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        configureTimePicker()
        configureButtonSet(binding.timePicker)
        configureButtonCancel()
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
        binding.buttonSet.run {
            setOnClickListener {
                val minute = timePicker.minute
                val hour = timePicker.hour

                setAlarmFor(minute, hour)

                /*
                cancelNotification(currentNotificationId)

                val pendingIntent = createPendingIntentFor<WakeActivity>()

                currentNotificationId = Random().nextInt()

                fireNotification(
                    currentNotificationId!!,
                    buildNotification(
                        channelId = NOTIFICATION_CHANNEL_ID,
                        title = "Title",
                        content = "Content $hour:$minute",
                        priority = NotificationCompat.PRIORITY_DEFAULT,
                        onClickIntent = pendingIntent,
                    )
                )

                setAlarmStatus(hour, minute)*/
            }
        }

    private fun setAlarmFor(minute: Int, hour: Int) {

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = createBroadcastIntent<AlarmReceiver>("senex.intent.action.START_ALARM")

        val delaySeconds = 5
        val alarmTime = System.currentTimeMillis() + delaySeconds * 1000
        log("sending")

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    private inline fun <reified T> Context.createBroadcastIntent(action: String): PendingIntent {
        currentNotificationId = Random().nextInt()

        val notification = buildNotification(
            channelId = NOTIFICATION_CHANNEL_ID,
            title = "Title",
            content = "Content",
            priority = NotificationCompat.PRIORITY_DEFAULT,
            onClickIntent = createImplicitPendingIntent<WakeActivity>(),
        )

        val intent = Intent(this, T::class.java)
        intent.action = action
        intent.putExtra("notification", notification)

        return PendingIntent.getBroadcast(this, 0, intent, 0)
    }

    private fun configureButtonCancel(): Unit =
        binding.buttonCancel.run {
            setOnClickListener {
                currentNotificationId?.let {
                    cancelNotification(it)
                    setAlarmStatusDefault()
                }
            }
        }

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

    private fun setAlarmStatusDefault() {
        binding.alarmStatus.text =
            getString(R.string.message_alarm_is_not_set)
    }

    private fun setAlarmStatus(hour: Int, minute: Int) {
        binding.alarmStatus.text =
            getString(R.string.message_alarm_status, hour, minute)
    }
}



