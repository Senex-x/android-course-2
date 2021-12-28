package com.senex.androidlab1.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.senex.androidlab1.databinding.FragmentListBinding
import com.senex.androidlab1.ui.fragments.list.recycler.ListRecyclerAdapter
import com.senex.androidlab1.ui.fragments.list.recycler.ListTouchHelper
import com.senex.androidlab1.utils.ListItemDecoration
import com.senex.androidlab1.viewmodels.MainViewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

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
            initFab()
        }

        return binding.root
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
                binding.noItemsText.visibility =
                    if (it.isEmpty()) View.VISIBLE else View.GONE

                listAdapter.submitList(it)
            }

            adapter = listAdapter

            layoutManager = LinearLayoutManager(
                requireContext()
            )

            addItemDecoration(
                ListItemDecoration(20)
            )
        }
    }

    private fun FragmentListBinding.initFab() {
        floatingActionButton.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections
                    .actionListFragmentToAddEditFragment()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainViewModel.removeOnListChangeListener()
        // In my case there is no reason to stop all coroutines operating with database,
        // since they are affecting neither UI elements nor instance of ListFragment.
        // Simple unsubscribing from change listening is enough.
        // mainViewModel.onDestroy()
    }
}