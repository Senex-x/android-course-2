package com.senex.androidlab1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.senex.androidlab1.models.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): NoteDao
}

object AppDatabaseMain {
    private var database_: AppDatabase? = null
    val database: AppDatabase
        get() = database_!!

    fun init(context: Context) {
        if (database_ == null)
            database_ = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database-main"
            ).allowMainThreadQueries()
                .build()
    }
}

