package com.senex.androidlab1.repository

import com.senex.androidlab1.R
import com.senex.androidlab1.models.Genre
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.toMillis

object TrackRepository {
    private final val trackList = listOf(
        Track(
            1,
            "Dr. Online",
            "Cool music track which you will definitely like",
            Genre.INDUSTRIAL,
            R.drawable.zeromancer_eurotrash_cover,
            R.raw.zeromancer_dr_online,
            toMillis(3, 18)
        )
    )

    fun getAll() = trackList

    fun getByPrimaryKey(key: Long) = trackList
        .find { track -> track.id == key }
}