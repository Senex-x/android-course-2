package com.senex.androidlab1.views.fragments.list.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ListItemNoteBinding
import com.senex.androidlab1.models.Note

class ListRecyclerAdapter(
    private val onItemDelete: (position: Int) -> Unit
) : ListAdapter<Note, ListRecyclerAdapter.ViewHolder>(NoteDiffCallback) {

    class ViewHolder(
        itemView: View,
        private val onItemDelete: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemNoteBinding.bind(itemView)

        fun bind(user: Note) =
            binding.run {
                header.text = user.header
                content.text = user.content
                deleteButton.setOnClickListener {
                    onItemDelete(layoutPosition)
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_note, parent, false),
        onItemDelete
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int
    ) = holder.bind(
        getItem(currentPosition)
    )
}