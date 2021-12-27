package com.senex.androidlab1.views.fragments.player.controls

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.FragmentPlayerControlsBinding
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.player.PlayerState
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

            musicService.subscribeForStateChange(stateListener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // TODO: Inspect option
        }
    }

    private val stateListener: (PlayerState) -> Unit = {
        com.senex.androidlab1.utils.log("Music player state has changed to: $it")

        binding.displayPlayerState(it)
    }

    private fun FragmentPlayerControlsBinding.displayPlayerState(state: PlayerState) {
        val icon = if (state == PlayerState.PLAYING)
            R.drawable.ic_pause_24 else R.drawable.ic_play_24

        playPauseButton.icon = requireContext().getThemedIcon(icon)
    }

    private fun FragmentPlayerControlsBinding.initPlayPauseButton() {
        playPauseButton.setOnClickListener {
            musicService.run {
                if (isPlaying) {
                    pause()
                } else {
                    resume()
                }
            }
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
        musicService.unsubscribeFromStateChange(stateListener)
    }
}
