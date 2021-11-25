package com.senex.androidlab1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey
    val notificationId: Int,
    val hour: Int,
    val minute: Int,
    val rtcTime: Long,
)
