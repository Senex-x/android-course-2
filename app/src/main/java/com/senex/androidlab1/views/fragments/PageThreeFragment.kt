package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.senex.androidlab1.databinding.FragmentPageThreeBinding

class PageThreeFragment : Fragment() {
    private var _binding: FragmentPageThreeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageThreeBinding
            .inflate(inflater, container, false)

        return binding.apply { init() }.root
    }

    private fun FragmentPageThreeBinding.init() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}