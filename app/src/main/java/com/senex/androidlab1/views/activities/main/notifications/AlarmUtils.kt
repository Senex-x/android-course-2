package com.senex.androidlab1.views.activities.main.notifications

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

internal inline fun <reified T> Context.enableBootReceiver() {
    setReceiverEnabledState<T>(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
}

internal inline fun <reified T> Context.disableBootReceiver() {
    setReceiverEnabledState<T>(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
}

private inline fun <reified T> Context.setReceiverEnabledState(stateCode: Int) {
    val receiver = ComponentName(this, T::class.java)

    packageManager.setComponentEnabledSetting(
        receiver,
        stateCode,
        PackageManager.DONT_KILL_APP
    )
}