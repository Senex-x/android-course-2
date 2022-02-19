package com.senex.androidlab1.database

import androidx.room.TypeConverter
import java.util.*

class MainConverter {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? =
        value?.let { Date(value) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? =
        date?.time

    @TypeConverter
    fun timestampToCalendar(value: Long?): Calendar? =
        value?.let {
            Calendar.getInstance().apply {
                time = Date(value)
            }
        }

    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar?): Long? =
        calendar?.time?.time
}