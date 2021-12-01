package com.senex.androidlab1.views.fragments.info

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.FragmentMusicInfoBinding
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.formatTime
import com.senex.androidlab1.utils.toast

class TrackInfoFragment : Fragment() {
    private val args: TrackInfoFragmentArgs by navArgs()
    private var _binding: FragmentMusicInfoBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var currentTrack: Track
    private var isMusicPlaying = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMusicInfoBinding.inflate(inflater, container, false)

        val nullableTrack = TrackRepository.get(args.trackId)

        if (nullableTrack != null) {
            currentTrack = nullableTrack
        } else {
            requireContext().toast("Unknown track identifier")
            findNavController().popBackStack()
        }

        mediaPlayer = MediaPlayer.create(
            requireContext(),
            currentTrack.trackRes
        )

        binding.run {
            initTextViews()
            initPlayButton()
            initNextButton()
            initPrevButton()
        }

        return binding.root
    }

    private fun FragmentMusicInfoBinding.initTextViews() {
        imageTrackCover.setImageResource(currentTrack.coverRes)
        trackName.text = currentTrack.trackName
        trackArtistName.text = currentTrack.artistName
        trackReleaseYear.text = currentTrack.releaseYear.toString()
        trackDuration.text = formatTime(currentTrack.durationMillis)
        genreName.text = currentTrack.genre.value
        genreDescription.text = currentTrack.genre.desc
    }

    private fun FragmentMusicInfoBinding.initPlayButton() {
        playPauseButton.apply {
            setOnClickListener {
                if (isMusicPlaying) {
                    mediaPlayer.pause()

                    icon = setThemedIcon(R.drawable.ic_play_24)
                } else {
                    mediaPlayer.start()

                    icon = setThemedIcon(R.drawable.ic_pause_24)
                }

                isMusicPlaying = !isMusicPlaying
            }
        }
    }

    private fun FragmentMusicInfoBinding.initNextButton() {
        nextButton.setOnClickListener {
            val nextTrack = TrackRepository.getNextFor(currentTrack.id)

            findNavController().navigate(
                TrackInfoFragmentDirections
                    .actionMusicInfoFragmentSelf(
                        nextTrack.id
                    )
            )
        }
    }

    private fun FragmentMusicInfoBinding.initPrevButton() {
        previousButton.setOnClickListener {
            val prevTrack = TrackRepository.getPrevFor(currentTrack.id)

            findNavController().navigate(
                TrackInfoFragmentDirections
                    .actionMusicInfoFragmentSelf(
                        prevTrack.id
                    )
            )
        }
    }

    override fun onStop() {
        super.onStop()

        mediaPlayer.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    // Get resource themed accordingly to an activity's theme
    private fun setThemedIcon(@DrawableRes id: Int) =
        ContextCompat.getDrawable(
            requireActivity(),
            id
        )
}