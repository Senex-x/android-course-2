package com.senex.androidlab1.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.senex.androidlab1.R
import com.senex.androidlab1.database.Converters
import java.util.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val nickname: String,
    val imageId: Int? = null,
    @ColumnInfo(name = "is_verified")
    val isVerified: Boolean = false,
    val status: String = "",
    val email: String,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "birth_date")
    val birthDate: Date,
    val description: String = ""
)
