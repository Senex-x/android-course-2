package com.senex.androidlab1.database

import androidx.room.TypeConverter
import java.util.*

class MainConverter {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun timestampToCalendar(value: Long?): Calendar? {
        return value?.let {
            Calendar.getInstance().apply {
                time = Date(value)
            }
        }
    }

    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar?): Long? {
        return calendar?.time?.time
    }
}