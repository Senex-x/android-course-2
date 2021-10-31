package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.models.User
import com.senex.androidlab1.recyclers.adapters.UserRecyclerAdapter
import com.senex.androidlab1.utils.log
import java.util.*

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        initRecyclerView(binding.listRecyclerMain)

        return binding.root
    }

    private fun initRecyclerView(recycler: RecyclerView) {
        recycler.layoutManager = LinearLayoutManager(requireContext())
        //studentsList = generateRandomCardsData()

        recycler.adapter = UserRecyclerAdapter(
            listOf(User(1, "nicknam5678678e", email = "lul", birthDate = Date())),
        )
        //studentsList = generateRandomCardsData()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}