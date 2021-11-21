package com.senex.androidlab1.views.activities.main.observers

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import com.senex.androidlab1.utils.log


class VolumeObserver(
    context: Context,
    handler: Handler
) : ContentObserver(handler) {

    private val audioManager = context
        .getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onChange(selfChange: Boolean) {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        log("Volume now is $currentVolume")
    }

    override fun deliverSelfNotifications() = false
}