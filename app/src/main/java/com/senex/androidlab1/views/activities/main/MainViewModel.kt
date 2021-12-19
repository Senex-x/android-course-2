package com.senex.androidlab1.views.activities.main

import androidx.lifecycle.ViewModel
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.repositories.NoteRepository

class MainViewModel : ViewModel() {
    private val noteDao = AppDatabaseMain.database.noteDao()
    private val notes = NoteRepository.getAll().toMutableList()

    fun add(note: Note) {
        notes.add(note)
        noteDao.insert(note)
    }

    fun get(index: Int): Note {
        return notes[index]
    }

    fun get(id: Long): Note? {
        return notes.find { note -> note.id == id }
    }

    fun getAll(): List<Note> {
        return notes
    }

    fun update(note: Note) {
        notes[notes.indexOf(note)] = note
        noteDao.update(note)
    }

    fun removeAt(index: Int) {
        noteDao.delete(
            notes.removeAt(index)
        )
    }

    fun swap(fromIndex: Int, toIndex: Int) {
        notes.add(
            toIndex,
            notes.removeAt(fromIndex)
        )
    }
}