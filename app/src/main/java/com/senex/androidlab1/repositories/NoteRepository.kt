package com.senex.androidlab1.repositories

import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.utils.log
import kotlinx.coroutines.*

class NoteRepository(
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    private val noteDao = AppDatabaseMain.database.noteDao()

    fun insertAsync(note: Note): Deferred<Long> {
        return coroutineScope.async {
            noteDao.insert(note)
        }
    }

    fun insertBlocking(note: Note): Long {
        return runBlocking {
            noteDao.insert(note)
        }
    }

    fun insertAll(vararg notes: Note) {
        coroutineScope.launch {
            noteDao.insertAll(*notes)
        }
    }

    fun getAsync(id: Long): Deferred<Note?> {
        return coroutineScope.async {
            noteDao.get(id)
        }
    }

    fun getBlocking(id: Long): Note? {
        return runBlocking {
            noteDao.get(id)
        }
    }

    fun getAllAsync(): Deferred<List<Note>> {
        return coroutineScope.async {
            noteDao.getAll()
        }
    }

    fun getAllBlocking(): List<Note> {
        return runBlocking {
            noteDao.getAll()
        }
    }

    fun update(note: Note) {
        coroutineScope.launch {
            noteDao.update(note)
        }
    }

    fun delete(id: Long) {
        coroutineScope.launch {
            noteDao.delete(id)
        }
    }

    fun delete(note: Note) {
        delete(note.id!!)
    }

    fun deleteAll() {
        coroutineScope.launch {
            noteDao.deleteAll()
        }
    }

    fun close() {
        coroutineScope.cancel()
    }
}