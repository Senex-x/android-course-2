package com.senex.androidlab1.repository

import com.senex.androidlab1.R
import com.senex.androidlab1.models.Genre
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.toMillis
import java.util.*

object TrackRepository {
    fun getAll() = trackList

    fun get(id: Long) = trackList
        .find { track -> track.id == id }

    fun getNextFor(id: Long) =
        trackList[(getIndex(id) + 1) % trackList.size]

    fun getPrevFor(id: Long) =
        trackList[(getIndex(id) - 1 + trackList.size) % trackList.size]

    private fun getWithIndex(id: Long): IndexedValue<Track> {
        for ((index, track) in trackList.withIndex()) {
            if (track.id == id) {
                return IndexedValue(index, track)
            }
        }

        throw NoSuchElementException("Track with id: $id does not exist")
    }

    private fun getIndex(id: Long) =
        getWithIndex(id).index

    // Better use savedInstanceState inside viewModel
    fun getTrackForFirstTime() =
        TrackRepository.getAll().first()

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