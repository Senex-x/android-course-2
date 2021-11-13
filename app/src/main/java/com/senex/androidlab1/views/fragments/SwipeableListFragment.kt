package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.adapters.swipeable.SwipeableRecyclerAdapter
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.FragmentSwipeableListBinding
import com.senex.androidlab1.utils.MarginItemDecoration

class SwipeableListFragment : Fragment() {
    private var _binding: FragmentSwipeableListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSwipeableListBinding.inflate(inflater, container, false)

        binding.recyclerMain.init()

        return binding.root
    }

    private fun RecyclerView.init() {
        layoutManager = LinearLayoutManager(
            requireContext()
        )

        adapter = SwipeableRecyclerAdapter(
            AppDatabaseMain.database.userDao().getAll()
        )

        addItemDecoration(
            MarginItemDecoration(20)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}