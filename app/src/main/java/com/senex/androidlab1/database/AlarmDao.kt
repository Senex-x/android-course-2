package com.senex.androidlab1.database

import androidx.room.*
import com.senex.androidlab1.model.Alarm

@Dao
interface AlarmDao {
    @Insert
    fun insert(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE notificationId = :id")
    fun get(id: Int): Alarm

    @Query("SELECT * FROM alarms")
    fun getAll(): List<Alarm>

    @Update
    fun update(alarm: Alarm)

    @Delete
    fun delete(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE notificationId = :id")
    fun deleteByKey(id: Int)

    @Query("DELETE FROM alarms")
    fun deleteAll()

    @Query("DELETE FROM alarms WHERE rtcTime < :rtc")
    fun deleteAllOutdated(rtc: Long)
}