package com.senex.androidlab1.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

private const val APP_DEBUG_COMMON_PREFIX = "app-debug"

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
    Log.d(
        APP_DEBUG_COMMON_PREFIX,
        message ?: "null"
    )

internal fun Fragment.log(message: String?) =
    Log.d(APP_DEBUG_COMMON_PREFIX,
        this::class.java.simpleName + ": " + message
    )

internal fun toMillis(minutes: Int, seconds: Int) =
    (minutes * 60 + seconds) * 1000

internal fun fromMillis(millis: Int) =
    millis / 1000 / 60 to millis / 1000 % 60

internal fun formatTime(time: Pair<Int, Int>): String {
    val minutesInt = time.second
    val minuteString = if (minutesInt < 10)
        "0$minutesInt" else minutesInt.toString()

    return "${time.first}:$minuteString"
}

internal fun formatTime(timeMillis: Int) =
    formatTime(fromMillis(timeMillis))

internal fun Context.getThemedIcon(@DrawableRes id: Int) =
    ContextCompat.getDrawable(
        this,
        id
    )
