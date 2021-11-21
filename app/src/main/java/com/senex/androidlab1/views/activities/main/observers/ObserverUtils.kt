package com.senex.androidlab1.views.activities.main.observers

import android.app.Activity
import android.content.Context
import android.database.ContentObserver
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.senex.androidlab1.views.activities.main.observers.volume.VolumeObserver

internal fun Context.registerObserver(
    observer: ContentObserver
): Unit = contentResolver.registerContentObserver(
    Settings.System.CONTENT_URI,
    true,
    observer
)

internal fun Context.unregisterObserver(
    observer: ContentObserver
): Unit = contentResolver.unregisterContentObserver(observer)

internal fun Context.configureVolumeObserver(mediaPlayer: MediaPlayer) =
    VolumeObserver(
        context = this,
        Handler(Looper.getMainLooper())
    ) { systemVolumeLevel ->
        val volumeLevel: Float = calculateVolumeLevel(systemVolumeLevel)
        mediaPlayer.setVolume(volumeLevel, volumeLevel)
    }

private fun calculateVolumeLevel(systemVolumeLevel: Int): Float {
    val section = 1 / 14f
    val value = section * (systemVolumeLevel - 1)
    return if (value == 1f) 0.05f else 1 - value
}