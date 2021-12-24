package com.senex.androidlab1.ui.activities.main

import androidx.lifecycle.ViewModel
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.repositories.NoteRepository

class MainViewModel : ViewModel() {
    private val noteDataSource = NoteRepository()
    private val notes = noteDataSource.getAll().toMutableList()

    fun add(note: Note) {
        val newNoteId = noteDataSource.insert(note)
        notes.add(noteDataSource.get(newNoteId)!!)
        notifySubscriber()
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
                noteDataSource.update(note)
            }
        }
        notifySubscriber()
    }

    fun removeAt(index: Int) {
        noteDataSource.delete(
            notes.removeAt(index)
        )
        notifySubscriber()
    }

    fun removeAll() {
        notes.clear()
        noteDataSource.deleteAll()
        notifySubscriber()
    }

    fun swap(fromIndex: Int, toIndex: Int) {
        notes.add(
            toIndex,
            notes.removeAt(fromIndex)
        )
        notifySubscriber()
    }

    private fun notifySubscriber() {
        subscriber?.invoke(notes.toList())
    }

    private var subscriber: ((List<Note>) -> Unit)? = null

    fun setOnListChangeListener(listener: (List<Note>) -> Unit) {
        subscriber = listener
    }
}