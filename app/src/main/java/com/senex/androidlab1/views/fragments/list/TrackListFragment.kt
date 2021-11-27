package com.senex.androidlab1.views.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.senex.androidlab1.databinding.FragmentMusicListBinding
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.views.fragments.list.recycler.MarginItemDecoration
import com.senex.androidlab1.views.fragments.list.recycler.TrackRecyclerAdapter

class TrackListFragment : Fragment() {
    private var _binding: FragmentMusicListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMusicListBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler() {
        binding.listRecyclerMain.apply {
            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = TrackRecyclerAdapter(
                TrackRepository.getAll()
            ) { clickedTrackId ->
                findNavController().navigate(
                    TrackListFragmentDirections
                        .actionMusicListFragmentToMusicInfoFragment(clickedTrackId)
                )
            }

            addItemDecoration(
                MarginItemDecoration(20)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}