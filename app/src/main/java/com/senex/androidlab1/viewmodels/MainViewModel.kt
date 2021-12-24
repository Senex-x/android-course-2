package com.senex.androidlab1.viewmodels

import com.senex.androidlab1.models.Note
import com.senex.androidlab1.repositories.NoteRepository

class MainViewModel : BaseViewModel() {
    private val noteDataSource = NoteRepository(coroutineScope)
    private val notes = noteDataSource.getAllBlocking().toMutableList()

    fun add(note: Note) {
        val insertedNoteId = noteDataSource.insertBlocking(note)
        notes.add(note.copy(id = insertedNoteId))
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

    private var subscriber: ((List<Note>) -> Unit)? = null

    private fun notifySubscriber() {
        subscriber?.invoke(notes.toList())
    }

    fun setOnListChangeListener(listener: (List<Note>) -> Unit) {
        subscriber = listener
    }
}