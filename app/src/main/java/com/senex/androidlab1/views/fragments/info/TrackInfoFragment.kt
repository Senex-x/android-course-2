package com.senex.androidlab1.views.fragments.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senex.androidlab1.databinding.FragmentTrackInfoBinding
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.formatTime
import com.senex.androidlab1.utils.toast

class TrackInfoFragment : Fragment() {
    private val args: TrackInfoFragmentArgs by navArgs()
    private var _binding: FragmentTrackInfoBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var thisTrack: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTrackInfoBinding.inflate(inflater, container, false)

        val nullableTrack = TrackRepository.get(args.trackId)

        if (nullableTrack != null) {
            thisTrack = nullableTrack
        } else {
            requireContext().toast("Unknown track identifier")
            findNavController().popBackStack()
        }

        binding.run {
            initTextViews()
        }

        return binding.root
    }

    private fun FragmentTrackInfoBinding.initTextViews() {
        thisTrack.let {
            imageTrackCover.setImageResource(it.coverRes)
            trackName.text = it.trackName
            trackArtistName.text = it.artistName
            trackAlbumName.text = it.albumName
            trackReleaseYear.text = it.releaseYear.toString()
            trackDuration.text = formatTime(it.durationMillis)
            genreName.text = it.genre.value
            genreDescription.text = it.genre.desc
        }
    }
}