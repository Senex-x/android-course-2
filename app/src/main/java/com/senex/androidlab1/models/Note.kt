package com.senex.androidlab1.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val header: String,
    val content: String,
    @TypeConverters(TypeConverter::class)
    val openingDate: Date,
    val targetDate: Date?,
    val longitude: String?,
    val latitude: String?,
)