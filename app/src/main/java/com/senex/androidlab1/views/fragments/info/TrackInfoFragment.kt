package com.senex.androidlab1.views.fragments.info

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
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
import com.senex.androidlab1.player.PlayerState
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.formatTime
import com.senex.androidlab1.utils.log
import com.senex.androidlab1.utils.toast

class TrackInfoFragment : Fragment() {
    private val args: TrackInfoFragmentArgs by navArgs()
    private var _binding: FragmentMusicInfoBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var thisTrack: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMusicInfoBinding.inflate(inflater, container, false)

        val nullableTrack = TrackRepository.get(args.trackId)

        if (nullableTrack != null) {
            thisTrack = nullableTrack
        } else {
            requireContext().toast("Unknown track identifier")
            findNavController().popBackStack()
        }

        binding.run {
            initTextViews()
            initPlayPauseButton()
            initNextButton()
            initPrevButton()
        }

        return binding.root
    }

    private fun FragmentMusicInfoBinding.initTextViews() {
        imageTrackCover.setImageResource(thisTrack.coverRes)
        trackName.text = thisTrack.trackName
        trackArtistName.text = thisTrack.artistName
        trackReleaseYear.text = thisTrack.releaseYear.toString()
        trackDuration.text = formatTime(thisTrack.durationMillis)
        genreName.text = thisTrack.genre.value
        genreDescription.text = thisTrack.genre.desc
    }

    private fun FragmentMusicInfoBinding.initPlayPauseButton() {
        playPauseButton.apply {
            setOnClickListener {
                musicService.run {
                    log(isPlaying.toString())

                    if (isPlaying) {
                        if (currentTrack == thisTrack) {
                            pause()
                        } else {
                            stop()
                        }
                    } else {
                        if (getState() != PlayerState.NOT_STARTED && currentTrack == thisTrack) {
                            resume()
                        } else {
                            play(thisTrack)
                        }
                    }

                    log(currentTrack.toString())
                }
            }
        }
    }

    private fun FragmentMusicInfoBinding.initNextButton() {
        nextButton.setOnClickListener {
            val nextTrack = TrackRepository.getNextFor(thisTrack.id)

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
            val prevTrack = TrackRepository.getPrevFor(thisTrack.id)

            findNavController().navigate(
                TrackInfoFragmentDirections
                    .actionMusicInfoFragmentSelf(
                        prevTrack.id
                    )
            )
        }
    }

    private fun FragmentMusicInfoBinding.displayPlayerState(state: PlayerState) {
        val icon = if (state == PlayerState.PLAYING)
            R.drawable.ic_pause_24 else R.drawable.ic_play_24

        playPauseButton.icon = setThemedIcon(icon)
    }

    override fun onResume() {
        super.onResume()

        initService()
    }

    override fun onStop() {
        super.onStop()

        requireActivity().unbindService(connection)
        musicService.unsubscribeForStateChange(stateListener)
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

    private lateinit var musicService: PlayerControlService

    private val stateListener: (PlayerState) -> Unit = {
        log("Player state changed to: $it")
        binding.displayPlayerState(it)
    }

    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            log("onServiceConnected()")
            musicService = (service as PlayerControlService.MainBinder).getService()

            musicService.subscribeForStateChange(stateListener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
}