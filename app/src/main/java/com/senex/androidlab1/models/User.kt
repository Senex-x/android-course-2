package com.senex.androidlab1.models

import java.util.*

data class User(
    val id: Long,
    val nickname: String,
    val isVerified: Boolean = false,
    val status: String = "",
    val email: String,
    val birthDate: Date,
    val description: String = ""
)
