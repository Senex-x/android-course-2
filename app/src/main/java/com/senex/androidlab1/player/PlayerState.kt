package com.senex.androidlab1.player

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class PlayerState : Parcelable {
    NOT_STARTED,
    PLAYING,
    PAUSED,
    STOPPED
}