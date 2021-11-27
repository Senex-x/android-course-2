package com.senex.androidlab1.repository

import com.senex.androidlab1.R
import com.senex.androidlab1.models.Genre
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.toMillis

object TrackRepository {
    fun getAll() = trackList

    fun getByPrimaryKey(key: Long) = trackList
        .find { track -> track.id == key }

    private val trackList = listOf(
        Track(
            1,
            "Dr. Online",
            "Zeromancer",
            2001,
            Genre.INDUSTRIAL,
            R.drawable.zeromancer_eurotrash_cover,
            R.raw.zeromancer_dr_online,
            toMillis(3, 18)
        ),
        Track(
            2,
            "Soul Atlas",
            "Clepsydra",
            2020,
            Genre.ELECTRONIC,
            R.drawable.clepsydra_sacred_symphonies_cover,
            R.raw.clepsydra_soul_atlas,
            toMillis(3, 26)
        ),
        Track(
            3,
            "Written In The Stars",
            "Tinie Tempah",
            2010,
            Genre.RAP_AND_HIP_HOP,
            R.drawable.tinie_tempah_disc_overy_cover,
            R.raw.tinie_tempah_written_in_the_stars,
            toMillis(3, 40)
        ),
        Track(
            4,
            "Meine Welt",
            "Peter Heppner",
            2012,
            Genre.POP,
            R.drawable.peter_heppner_my_heart_of_stone_cover,
            R.raw.peter_heppner_meine_welt,
            toMillis(3, 26)
        ),
    )
}