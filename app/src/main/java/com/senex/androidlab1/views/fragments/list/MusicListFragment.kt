package com.senex.androidlab1.views.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.FragmentMusicListBinding
import com.senex.androidlab1.models.Genre
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.utils.toMillis
import com.senex.androidlab1.utils.toast
import com.senex.androidlab1.views.fragments.list.recycler.TrackRecyclerAdapter

class MusicListFragment : Fragment() {
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

            adapter = TrackRecyclerAdapter(getTrackList()) {
                requireContext().toast("Item clicked")
            }
        }
    }

    private fun getTrackList(): List<Track> =
        mutableListOf(
            Track(
                1,
                "Dr. Online",
                "Cool music track which you will definitely like",
                Genre.INDUSTRIAL,
                R.drawable.zeromancer_eurotrash_cover,
                R.raw.zeromancer_dr_online,
                toMillis(3, 18)
            )
        )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}