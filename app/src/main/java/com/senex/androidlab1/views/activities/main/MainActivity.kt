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

import android.content.pm.PackageManager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.senex.androidlab1.views.activities.main.notifications.receivers.NotificationSender


private const val NOTIFICATION_CHANNEL_ID = "MAIN_NOTIFICATION_CHANNEL_ID"
private const val NOTIFICATION_CHANNEL_NAME = "MAIN_NOTIFICATION_CHANNEL_NAME"

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

                sendNotification()

                /*
                cancelNotification(currentNotificationId)

                val minute = timePicker.minute
                val hour = timePicker.hour

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

    private fun sendNotification() {

        // Get AlarmManager instance
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Intent part
        val intent = Intent(this, NotificationSender::class.java)
        intent.action = "FOO_ACTION"
        intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo")

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        // Alarm time
        val ALARM_DELAY_IN_SECOND = 5
        val alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L
        log("sending")


        // Set with system Alarm Service
        // Other possible functions: setExact() / setRepeating() / setWindow(), etc
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC,
            pendingIntent
        )

/*
        val alarmMgr: AlarmManager
        val alarmIntent: PendingIntent

        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, NotificationSender::class.java).let { intent ->
            intent.setAction("com.senex.androidlab1.alarms")
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 3000,
            alarmIntent
        )*/
    }


    private fun activateBroadcastReceiver() {
        val pm: PackageManager = packageManager
        val componentName = ComponentName(this, NotificationSender::class.java)
        pm.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        log("receiver activated")
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



