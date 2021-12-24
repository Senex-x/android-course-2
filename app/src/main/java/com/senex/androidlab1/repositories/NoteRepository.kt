package com.senex.androidlab1.repositories

import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.utils.log
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class NoteRepository(
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    private val noteDao = AppDatabaseMain.database.noteDao()

    // Inserts are blocking-only to ensure that changes are written into database,
    // even if user did close the application before coroutine completion.
    fun insertBlocking(note: Note): Long {
        return runBlocking {
            noteDao.insert(note)
        }
    }

    fun insertAllBlocking(vararg notes: Note) {
        runBlocking{
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

    // Inserts are blocking-only to ensure that changes are written into database,
    // even if user did close the application before coroutine completion.
    fun updateBlocking(note: Note) {
        runBlocking {
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