package com.senex.androidlab1.views.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.utils.ListItemDecoration
import com.senex.androidlab1.utils.toast
import com.senex.androidlab1.views.activities.main.MainViewModel
import com.senex.androidlab1.views.fragments.list.recycler.ListRecyclerAdapter
import com.senex.androidlab1.views.fragments.list.recycler.ListTouchHelper

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    // Lazy initialization is used here in order to avoid requirement to use var with lateinit
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
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
                requireContext().toast("Fab on click call")
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
            listAdapter = ListRecyclerAdapter(
                { clickedNote ->
                    findNavController().navigate(
                        ListFragmentDirections.actionListFragmentToEditFragment(
                            clickedNote.id!!
                        )
                    )

                }, { deletedNotePosition ->
                    mainViewModel.removeAt(deletedNotePosition)
                    submitList()
                }
            )

            submitList()

            ItemTouchHelper(
                ListTouchHelper(listTouchHelperAdapter)
            ).attachToRecyclerView(this)

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
            submitList()
        }

        override fun onItemDismiss(position: Int) {
            mainViewModel.removeAt(position)
            submitList()
        }
    }

    private fun submitList() {
        listAdapter.submitList(
            // Use toList() to retrieve a new link to it
            mainViewModel.getAll().toList()
        )
    }
}