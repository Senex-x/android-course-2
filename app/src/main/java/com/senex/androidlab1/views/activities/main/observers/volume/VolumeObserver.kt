package com.senex.androidlab1.views.activities.main.observers.volume

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler

class VolumeObserver(
    context: Context,
    handler: Handler,
    val onVolumeChangeListener: (newVolumeLevel: Int) -> Unit
) : ContentObserver(handler) {

    private val audioManager = context
        .getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onChange(selfChange: Boolean) {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        onVolumeChangeListener(currentVolume)
    }

    override fun deliverSelfNotifications() = false
}