package com.senex.androidlab1.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.ui.activities.main.MainViewModel
import com.senex.androidlab1.ui.fragments.list.recycler.ListRecyclerAdapter
import com.senex.androidlab1.ui.fragments.list.recycler.ListTouchHelper

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    // Delegated initialization is used here in order to avoid requirement to use var with lateinit
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var listAdapter: ListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.run {
            initRecyclerView()

            floatingActionButton.setOnClickListener {
                findNavController().navigate(
                    ListFragmentDirections
                        .actionListFragmentToAddEditFragment()
                )
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentListBinding.initRecyclerView() {
        listRecyclerMain.run {
            listAdapter = ListRecyclerAdapter { clickedNote ->
                    findNavController().navigate(
                        ListFragmentDirections
                            .actionListFragmentToAddEditFragment()
                            .setNoteId(clickedNote.id!!)
                    )
                }

            ItemTouchHelper(
                ListTouchHelper(listTouchHelperAdapter)
            ).attachToRecyclerView(this)

            mainViewModel.setOnListChangeListener {
                listAdapter.submitList(it)
            }

            listAdapter.submitList(
                mainViewModel.getAll().toList()
            )

            adapter = listAdapter

            layoutManager = LinearLayoutManager(
                requireContext()
            )

            addItemDecoration(
                ListItemDecoration(20)
            )
        }
    }

    private val listTouchHelperAdapter = object : ListTouchHelper.Adapter {

        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            mainViewModel.swap(fromPosition, toPosition)
        }

        override fun onItemDismiss(position: Int) {
            mainViewModel.removeAt(position)
        }
    }

    private fun submitList() {
        listAdapter.submitList(
            // Use toList() to retrieve a new link to it
            mainViewModel.getAll().toList()
        )
    }
}