package com.senex.androidlab1.database

import androidx.room.*
import com.senex.androidlab1.models.Note

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note): Long

    @Insert
    fun insertAll(vararg notes: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun get(id: Long): Note?

    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Query("DELETE FROM notes")
    fun deleteAll()
}