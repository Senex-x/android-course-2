package com.senex.androidlab1.repositories

import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.models.Note
Miimport com.senex.androidlab1.utils.log
import kotlinx.coroutines.*

class NoteRepository(
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    private val noteDao = AppDatabaseMain.database.noteDao()

    fun insert(note: Note): Long {
        coroutineScope.launch {
            noteDao.insert(note)
        }
        return -1
    }

    fun insertAll(vararg notes: Note) {
        coroutineScope.launch {
            noteDao.insertAll(*notes)
        }
    }

    fun get(id: Long): Note? {
        log("get call")
        val res = coroutineScope.async {
            noteDao.get(id)
            log("get got data")
            delay(1000)
        }
        log("after call")

        return null
    }

    fun getAll(): List<Note> {
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