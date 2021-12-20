package com.senex.androidlab1.views.activities.main

import androidx.lifecycle.ViewModel
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.repositories.NoteRepository

class MainViewModel : ViewModel() {
    private val noteDao = AppDatabaseMain.database.noteDao()
    private val notes = NoteRepository.getAll().toMutableList()

    fun add(note: Note) {
        val newNoteId = noteDao.insert(note)
        notes.add(noteDao.get(newNoteId)!!)
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
        for((i, oldNote) in notes.withIndex()) {
            if(oldNote.id == note.id) {
                notes[i] = note
                noteDao.update(note)
            }
        }
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