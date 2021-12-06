package com.senex.androidlab1.models

import android.os.Parcel
import android.os.Parcelable
import com.senex.androidlab1.utils.log
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlayerControlAction(val action: Action) : Parcelable {

    companion object : Parceler<PlayerControlAction> {
        override fun PlayerControlAction.write(parcel: Parcel, flags: Int) {
            parcel.writeString(action.toString())
        }

        override fun create(parcel: Parcel): PlayerControlAction {
            val action = Action.valueOf(parcel.readString()!!)
            log("Create with $action")

            return PlayerControlAction(action)
        }
    }
}

enum class Action {
    START,
    STOP,
    NEXT,
    PREV,
}