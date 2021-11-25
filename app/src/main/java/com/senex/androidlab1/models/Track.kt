package com.senex.androidlab1.models

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Track(
    val id: Long,
    val name: String,
    val description: String,
    val genre: Genre,
    @DrawableRes
    val coverRes: Int,
    @RawRes
    val trackRes: Int,
    val durationMillis: Int,
)

enum class Genre(name: String) {
    ROCK("Rock"),
    METAL("Metal"),
    INDUSTRIAL("Industrial"),
    ALTERNATIVE("Alternative"),
    PUNK("Punk"),
    ELECTRONICS("Electronics"),
    POST_HARDCORE("Post hardcore"),
}