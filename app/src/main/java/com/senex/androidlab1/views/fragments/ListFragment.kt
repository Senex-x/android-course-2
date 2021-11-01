package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.adapters.UserRecyclerAdapter
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.utils.MarginItemDecoration


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

        binding.listRecyclerMain.init()

        return binding.root
    }

    private fun RecyclerView.init() {
        layoutManager = LinearLayoutManager(
            requireContext()
        )
        adapter = UserRecyclerAdapter(
            AppDatabaseMain.database.userDao().getAll()
        ) {
            findNavController().navigate(
                ListFragmentDirections
                    .actionListFragmentToInfoFragment(
                        it.id!! // id cannot be null
                    )
            )
        }
        addItemDecoration(
            MarginItemDecoration(20)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}