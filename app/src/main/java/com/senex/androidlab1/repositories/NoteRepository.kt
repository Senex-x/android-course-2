package com.senex.androidlab1.repositories

import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note

object NoteRepository {
    fun add(note: Note) {
        AppDatabaseMain.database.noteDao().insert(note)
    }

    fun get(id: Long): Note? {
        return AppDatabaseMain.database.noteDao().get(id)
    }

    fun getAll(): List<Note> {
        return AppDatabaseMain.database.noteDao().getAll()
    }

    fun update(note: Note) {
        AppDatabaseMain.database.noteDao().update(note)
    }

    fun delete(id: Long) {
        AppDatabaseMain.database.noteDao().delete(id)
    }

    fun deleteAll() {
        AppDatabaseMain.database.noteDao().deleteAll()
    }
}