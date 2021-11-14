package com.senex.androidlab1.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.adapters.interactive.ListRecyclerAdapter
import com.senex.androidlab1.adapters.interactive.ListTouchHelper
import com.senex.androidlab1.database.AppDatabaseMain
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.models.User
import com.senex.androidlab1.utils.MarginItemDecoration
import com.senex.androidlab1.views.dialogs.AddItemDialog
import java.util.*


class ListFragment : Fragment() {
    private val runtimeUsers = AppDatabaseMain.database.userDao().getAll().toMutableList()
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

        val index = if (
            position.isEmpty() ||
            position.toInt() > runtimeUsers.size
        ) runtimeUsers.size else position.toInt()

        listAdapter.submitList(
            runtimeUsers.apply {
                add(
                    index,
                    User(Random().nextLong(), name, description)
                )
            }.toList()
        )
    }

    private fun RecyclerView.init() {
        listAdapter = ListRecyclerAdapter { deletedItemPosition ->
            listAdapter.submitList(
                runtimeUsers.apply {
                    removeAt(deletedItemPosition)
                }.toList()
            )
        }

        listAdapter.submitList(
            runtimeUsers.toList()
        )

        ItemTouchHelper(
            ListTouchHelper(
                object : ListTouchHelper.Adapter {
                    override fun onItemMove(fromPosition: Int, toPosition: Int) {
                        listAdapter.submitList(
                            runtimeUsers.apply {
                                add(
                                    toPosition,
                                    runtimeUsers.removeAt(fromPosition)
                                )
                            }.toList()
                        )
                    }

                    override fun onItemDismiss(position: Int) {
                        listAdapter.submitList(
                            runtimeUsers.apply {
                                removeAt(position)
                            }.toList()
                        )
                    }
                }
            )
        ).attachToRecyclerView(this)

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