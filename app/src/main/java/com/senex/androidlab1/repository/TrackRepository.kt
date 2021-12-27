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
        getAll().first()

    private val trackList = listOf(
        Track(
            1,
            "Dr. Online",
            "Zeromancer",
            "Eurotrash",
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
            "Sacred Symphonies",
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
            "Heart of Stone",
            2012,
            Genre.POP,
            R.drawable.peter_heppner_my_heart_of_stone_cover,
            R.raw.peter_heppner_meine_welt,
            toMillis(3, 26)
        ),
        Track(
            4,
            "Skyline",
            "Sensi Affect",
            "Hypnotize",
            2019,
            Genre.ELECTRONIC,
            R.drawable.sensi_affect_hypnotize_cover,
            R.raw.sensi_affect_skyline,
            toMillis(3, 29)
        ),
        Track(
            5,
            "Bad Things",
            "Lacuna Coil",
            "Live from the Apocalypse",
            2021,
            Genre.ALT_METAL,
            R.drawable.lacuna_coil_live_from_the_apocalypse_cover,
            R.raw.lacuna_coil_bad_things,
            toMillis(3, 8)
        ),
        Track(
            6,
            "B12",
            "Grey Daze",
            "Amends",
            2020,
            Genre.ROCK,
            R.drawable.grey_daze_amends_cover,
            R.raw.grey_daze_b12,
            toMillis(3, 33)
        ),
        Track(
            7,
            "Brand New Day",
            "Ryan Star",
            "11:59",
            2010,
            Genre.ROCK,
            R.drawable.ryan_star_1159_cover,
            R.raw.ryan_star_brand_new_day,
            toMillis(3, 13)
        ),
        Track(
            8,
            "24",
            "Jem",
            "Finally Woken",
            2004,
            Genre.POP,
            R.drawable.jem_finally_woken_cover,
            R.raw.jem_24,
            toMillis(3, 54)
        ),
        Track(
            9,
            "Firesign",
            "Dynazty",
            "Firesign",
            2018,
            Genre.ALT_METAL,
            R.drawable.dynazty_firesign_cover,
            R.raw.dynazty_firesign,
            toMillis(4, 1)
        ),
        Track(
            10,
            "Enclosed",
            "plenka",
            "Enclosed",
            2021,
            Genre.ELECTRONIC,
            R.drawable.plenka_enclosed_cover,
            R.raw.plenka_enclosed,
            toMillis(1, 36)
        )
    )
}