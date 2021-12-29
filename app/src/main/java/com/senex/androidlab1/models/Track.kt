package com.senex.androidlab1.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val releaseYear: Int,
    val genre: Genre,
    @DrawableRes
    val coverRes: Int,
    @RawRes
    val trackRes: Int,
    val durationMillis: Int,
): Parcelable

enum class Genre(
    val value: String,
    val desc: String,
) {
    ROCK("Rock", "Rock music is a broad genre of popular music that originated as \"rock and roll\" in the United States in the late 1940s and early 1950s, developing into a range of different styles in the mid-1960s and later, particularly in the United States and the United Kingdom. It has its roots in 1940s and 1950s rock and roll, a style that drew directly from the blues and rhythm and blues genres of African-American music and from country music."),
    METAL("Metal", ""),
    ALT_METAL("Alternative metal", "Alternative metal is a genre of heavy metal music that combines heavy metal with influences from alternative rock and other genres not normally associated with metal. Alternative metal bands are often characterized by heavily downtuned, mid-paced guitar riffs, a mixture of accessible melodic vocals and harsh vocals."),
    INDUSTRIAL("Industrial", "Industrial music is a genre of music that draws on harsh, transgressive or provocative sounds and themes."),
    ALTERNATIVE("Alternative", ""),
    PUNK("Punk", ""),
    ELECTRONIC("Electronics", "Electronic music is music that employs electronic musical instruments, digital instruments, or circuitry-based music technology in its creation."),
    POST_HARDCORE("Post hardcore", ""),
    POP("Pop", "Pop is a genre of popular music that originated in its modern form during the mid-1950s in the United States and the United Kingdom. The terms popular music and pop music are often used interchangeably, although the former describes all music that is popular and includes many disparate styles."),
    RAP_AND_HIP_HOP("Rap and hip hop", ""),
}