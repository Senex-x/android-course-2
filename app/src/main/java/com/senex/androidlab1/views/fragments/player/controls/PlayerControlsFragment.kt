package com.senex.androidlab1.views.fragments.player.controls

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.FragmentPlayerControlsBinding
import com.senex.androidlab1.player.OnStateChangeListener
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.player.State
import com.senex.androidlab1.utils.formatTime
import com.senex.androidlab1.utils.getThemedIcon

class PlayerControlsFragment : Fragment() {
    private var _binding: FragmentPlayerControlsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var musicService: PlayerControlService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayerControlsBinding.inflate(inflater, container, false)

        binding.run {
            initPlayPauseButton()
            initPrevButton()
            initNextButton()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().bindService(
            Intent(requireActivity(), PlayerControlService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicService = (service as PlayerControlService.MainBinder).getService()

            musicService.subscribeForStateChange(stateChangeListener)
            progressBarUpdater.run()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            releaseCallbacks()
        }
    }

    // ????????????????????????????????????????????????????????
    private val stateChangeListener: OnStateChangeListener = object : OnStateChangeListener {
        override fun onStateChange(newState: State) {
            binding.displayPlayerState(newState)
            TODO("Not yet implemented")
        }
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val progressBarUpdater = object : Runnable {
        override fun run() {
            mainHandler.postDelayed(this, 1000)

            val elapsedTime = musicService.getTrackElapsedDurationMillis()
            binding.trackElapsedDuration.text = formatTime(elapsedTime)
            binding.trackDurationProgressBar.setProgress(
                elapsedTime / 1000,
                true
            )
        }
    }

    private fun FragmentPlayerControlsBinding.displayPlayerState(
        state: State
    ) {
        val icon = if (state == State.PLAYING)
            R.drawable.ic_pause_24 else R.drawable.ic_play_24
        playPauseButton.icon = requireContext().getThemedIcon(icon)

        val currentTrack = musicService.currentTrack
        currentTrackName.text = currentTrack.trackName
        currentTrackArtist.text = currentTrack.artistName

        val trackDurationMillis = currentTrack.durationMillis
        trackDuration.text = formatTime(trackDurationMillis)
        trackDurationProgressBar.max = trackDurationMillis / 1000
    }

    private fun FragmentPlayerControlsBinding.initPlayPauseButton() {
        playPauseButton.setOnClickListener {
            musicService.resumeOrPause()
        }
    }

    private fun FragmentPlayerControlsBinding.initNextButton() {
        nextButton.setOnClickListener {
            musicService.next()
        }
    }

    private fun FragmentPlayerControlsBinding.initPrevButton() {
        previousButton.setOnClickListener {
            musicService.previous()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        releaseCallbacks()
    }

    private fun releaseCallbacks() {
        musicService.unsubscribeFromStateChange(stateChangeListener)
        mainHandler.removeCallbacks(progressBarUpdater)
    }
}
