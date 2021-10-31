package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private val args: InfoFragmentArgs by navArgs()
    private var _binding: FragmentInfoBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val user = AppDatabaseMain.database.userDao().get(args.userId)

        binding.infoTextViewNickname.text = user.nickname
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}