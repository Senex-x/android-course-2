package com.senex.androidlab1.views.fragments.info

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
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
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.player.notifications.buildNotification
import com.senex.androidlab1.player.notifications.sendNotification
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.formatTime
import com.senex.androidlab1.utils.fromMillis
import com.senex.androidlab1.utils.log
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

    override fun onResume() {
        super.onResume()

        musicService?.let {
            currentTrack = TrackRepository.get(it.currentTrackId!!)!!
            //updateSeekBar(it.getService().mediaPlayer)
        }
        initService()
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

    private fun initService() = requireActivity().bindService(
        Intent(context, PlayerControlService::class.java),
        connection,
        Context.BIND_AUTO_CREATE
    )

    private var musicService: PlayerControlService? = null
    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            log("onServiceConnected()")
            musicService = (service as PlayerControlService.MainBinder).getService()
            musicService.also {
                log("also")

                context?.let { context ->
                    log("let")
                    activity?.intent?.removeExtra("TRACK")
                    buildNotification(requireContext()).sendNotification(requireContext(), currentTrack.id)
                }

                if (currentTrack.id != it?.currentTrackId) {
                    if (it?.mediaPlayer?.isPlaying == true) musicService?.stop()
                    musicService?.play(currentTrack)
                }

                it?.mediaPlayer.let { it1 ->
                    binding.trackDuration.text = fromMillis(it1?.duration!!).toString()
                }
                //it?.currentTrackId = track.id
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
}