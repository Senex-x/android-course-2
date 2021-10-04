package com.senex.androidlab1.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

internal fun Context.toast(message: String?) =
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    } ?: Unit

internal fun Context.toast(messageId: Int?) =
    messageId?.let {
        toast(this.getString(it))
    } ?: Unit

internal fun Int?.toast(context: Context) =
    context.toast(this)

internal fun String?.toast(context: Context) =
    context.toast(this)

internal fun log(message: String?) =
    Log.d("App-debug", message ?: "null")

private fun String.present() =
    isNotEmpty() && isNotBlank()