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
import android.app.AlarmManager

import android.os.SystemClock

import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.content.IntentFilter

import com.senex.androidlab1.utils.log
import android.widget.Toast

import android.content.pm.PackageManager

import android.content.ComponentName
import android.view.View
import android.widget.Filter
import android.widget.EditText





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

       // activateBroadcastReceiver()

/*
        val intent = Intent()
        // In reality, you would want to have a unique variable for the request
        // code
        // In reality, you would want to have a unique variable for the request
        // code
        intent.action = "ALARM"

        val sender = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            0
        )


        val elapsedRealtime = SystemClock.elapsedRealtime()
        val triggerTime = elapsedRealtime + 3000
        // Get the AlarmManager service
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        am.set(AlarmManager.ELAPSED_REALTIME, triggerTime, sender)
*/
        /*
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, NotificationSender::class.java)


        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val triggerTime = SystemClock.elapsedRealtime() + 3000

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
*/
        log("sending")

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



