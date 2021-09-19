package com.senex.androidlab1.utils

import android.util.Log

internal fun log(message: String?) =
    Log.d("app-debug", message ?: "null")

@JvmName("logExt")
internal fun String?.log() =
    log(this)