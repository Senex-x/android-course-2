package com.senex.androidlab1.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class TypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromIntList(intList: List<Int>): String =
        Gson().toJson(
            intList,
            object : TypeToken<List<Int>>() {}.type
        )

    @TypeConverter
    fun toIntList(intListJson: String): List<Int> =
        Gson().fromJson(
            intListJson,
            object : TypeToken<List<Int>>() {}.type
        )
}