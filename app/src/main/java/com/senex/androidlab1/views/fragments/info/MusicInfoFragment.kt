package com.senex.androidlab1.views.fragments.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senex.androidlab1.databinding.FragmentMusicInfoBinding
import com.senex.androidlab1.models.Track
import com.senex.androidlab1.repository.TrackRepository
import com.senex.androidlab1.utils.toast

class MusicInfoFragment : Fragment() {
    private val args: MusicInfoFragmentArgs by navArgs()
    private var _binding: FragmentMusicInfoBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var track: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMusicInfoBinding.inflate(inflater, container, false)

        val nullableTrack = TrackRepository.getByPrimaryKey(args.trackId)

        if (nullableTrack == null) {
            requireContext().toast("Unknown track identifier")
            findNavController().popBackStack()
        } else {
            track = nullableTrack
        }

        initTextViews()

        return binding.root
    }

    private fun initTextViews() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}