package com.senex.androidlab1.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

internal fun log(message: String?) =
    Log.d("app-debug", message ?: "null")

@JvmName("logExt")
internal fun String?.log() =
    log(this)

internal fun Context.toast(message: String?) =
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    } ?: Unit