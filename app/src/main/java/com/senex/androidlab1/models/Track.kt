package com.senex.androidlab1.models

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val releaseYear: Int,
    val genre: Genre,
    @DrawableRes
    val coverRes: Int,
    @RawRes
    val trackRes: Int,
    val durationMillis: Int,
)

enum class Genre(
    val value: String,
    val desc: String,
) {
    ROCK("Rock", ""),
    METAL("Metal", ""),
    INDUSTRIAL("Industrial", "Industrial music is a genre of music that draws on harsh, transgressive or provocative sounds and themes."),
    ALTERNATIVE("Alternative", ""),
    PUNK("Punk", ""),
    ELECTRONIC("Electronics", "Electronic music is music that employs electronic musical instruments, digital instruments, or circuitry-based music technology in its creation."),
    POST_HARDCORE("Post hardcore", ""),
    POP("Pop", ""),
    RAP_AND_HIP_HOP("Rap and hip hop", ""),
}