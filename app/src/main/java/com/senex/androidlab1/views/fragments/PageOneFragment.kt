package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.senex.androidlab1.databinding.FragmentPageOneBinding

class PageOneFragment : Fragment() {
    private var _binding: FragmentPageOneBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageOneBinding
            .inflate(inflater, container, false)

        return binding.apply { init() }.root
    }

    private fun FragmentPageOneBinding.init() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}