package com.senex.androidlab1.views.fragments.list

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.senex.androidlab1.databinding.FragmentTrackListBinding
import com.senex.androidlab1.player.PlayerControlService
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.views.fragments.list.recycler.MarginItemDecoration
import com.senex.androidlab1.views.fragments.list.recycler.TrackRecyclerAdapter

class TrackListFragment : Fragment() {
    private var _binding: FragmentTrackListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var musicService: PlayerControlService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTrackListBinding.inflate(inflater, container, false)

        binding.initRecycler()

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
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    private fun FragmentTrackListBinding.initRecycler() {
        listRecyclerMain.apply {
            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = TrackRecyclerAdapter(
                TrackRepository.getAll(),
                ::onTrackClicked,
                ::navigateToMusicInfoFragment,
            )

            addItemDecoration(
                MarginItemDecoration(20)
            )
        }
    }

    private fun onTrackClicked(trackId: Long) =
        musicService.resumeOrPauseIfCurrentOrPlayNew(trackId)

    private fun navigateToMusicInfoFragment(trackId: Long) {
        findNavController().navigate(
            TrackListFragmentDirections
                .actionMusicListFragmentToMusicInfoFragment(
                    trackId
                )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}