package com.senex.androidlab1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.senex.androidlab1.models.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(MainConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

object AppDatabaseMain {
    private var database_: AppDatabase? = null

    operator fun invoke() = database_!!

    fun init(context: Context) {
        if (database_ == null)
            database_ = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database-main"
            ).build()
    }
}

