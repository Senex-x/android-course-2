package com.senex.androidlab1.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val description: String = "",
    val imageResources: List<Int> = emptyList()
)
