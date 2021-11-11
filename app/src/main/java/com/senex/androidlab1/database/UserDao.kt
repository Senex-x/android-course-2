package com.senex.androidlab1.database

import androidx.room.*
import com.senex.androidlab1.models.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteByKey(id: Long)

    @Query("SELECT * FROM users WHERE id = :id")
    fun get(id: Long): User

    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("DELETE FROM users")
    fun deleteAll()
}