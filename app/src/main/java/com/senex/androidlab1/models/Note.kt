package com.senex.androidlab1.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.senex.androidlab1.database.MainConverter
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val header: String,
    val content: String,
    val openingDate: Calendar,
    val targetDate: Calendar?,
    val longitude: Double?,
    val latitude: Double?,
)