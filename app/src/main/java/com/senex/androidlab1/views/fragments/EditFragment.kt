package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.FragmentEditBinding

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        binding.editButtonUpdate.setOnClickListener(this::updateButtonOnClick)

        return binding.root
    }

    private fun updateButtonOnClick(view: View) {
        val description = binding.editEditTextDescription.text.toString()

        findNavController().navigate(
            R.id.action_editFragment_to_profileFragment,
            Bundle().apply {
                putString("description", description)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}