package com.senex.androidlab1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.senex.androidlab1.model.Alarm

@Database(entities = [Alarm::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}

object MainDatabase {
    private var instance_: AppDatabase? = null
    val instance: AppDatabase
        get() = instance_!!

    fun init(context: Context) {
        if (instance_ == null)
            instance_ = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database-main"
            ).allowMainThreadQueries()
                .build()
    }
}