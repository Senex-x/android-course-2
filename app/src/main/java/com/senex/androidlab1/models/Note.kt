package com.senex.androidlab1.models

import java.util.*

data class Note(
    val id: Long,
    val header: String,
    val content: String,
    val openingDate: Date,
)
