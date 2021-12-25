package com.senex.androidlab1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.repositories.NoteRepository

class MainViewModel : ViewModel() {
    private val noteDataSource = NoteRepository(viewModelScope)
    // I really dislike that here I get all notes by blocking the main thread.
    // I tried to figure out, how to avoid this blocking,
    // but didn't come up to any acceptable concise solution yet.
    private val notes = noteDataSource.getAllBlocking().toMutableList()

    fun add(note: Note) {
        val insertedNoteId = noteDataSource.insertBlocking(note)
        notes.add(note.copy(id = insertedNoteId))
        notifySubscriber()
    }

    fun addAll(vararg newNotes: Note) {
        noteDataSource.insertAllBlocking(*newNotes)
        notes.addAll(newNotes)
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
        for ((i, oldNote) in notes.withIndex()) {
            if (oldNote.id == note.id) {
                notes[i] = note
                noteDataSource.updateBlocking(note)
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
        notifySubscriber()
    }

    fun removeOnListChangeListener() {
        subscriber = null
    }

    override fun onCleared() {
        super.onCleared()
        // There is no need to manually cancel coroutines,
        // since the viewModelScope is lifecycle aware,
        // so it is canceled automatically.
        // noteDataSource.close()
    }
}