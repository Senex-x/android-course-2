package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.adapters.ListRecyclerAdapter
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.models.User
import com.senex.androidlab1.utils.MarginItemDecoration
import com.senex.androidlab1.views.dialogs.AddItemDialog
import java.util.*

class ListFragment : Fragment() {
    private lateinit var listAdapter: ListRecyclerAdapter
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.run {
            listRecyclerMain.init()

            floatingActionButton.setOnClickListener {
                val addItemDialog = AddItemDialog(onAddItemClick)

                addItemDialog.show(parentFragmentManager, "??")
            }
        }

        return binding.root
    }

    private val onAddItemClick = { name: String,
                                   description: String,
                                   position: String ->

        val mutableList = AppDatabaseMain.database.userDao().getAll().toMutableList()

        val index = if (
            position.isEmpty() ||
            position.toInt() > mutableList.size
        ) mutableList.size else position.toInt()

        listAdapter.submitList(
            mutableList.apply {
                add(
                    index,
                    User(Random().nextLong(), name, description)
                )
            }
        )
    }

    private fun RecyclerView.init() {
        listAdapter = ListRecyclerAdapter().apply {
            submitList(
                AppDatabaseMain.database.userDao().getAll()
            )
        }
        adapter = listAdapter

        layoutManager = LinearLayoutManager(
            requireContext()
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