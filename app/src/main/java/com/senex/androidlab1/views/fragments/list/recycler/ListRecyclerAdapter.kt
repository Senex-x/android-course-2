package com.senex.androidlab1.views.fragments.list.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ListItemNoteBinding
import com.senex.androidlab1.models.Note
import com.senex.androidlab1.utils.formatDate

class ListRecyclerAdapter(
    private val onItemClick: (note: Note) -> Unit,
    private val onItemDelete: (position: Int) -> Unit,
) : ListAdapter<Note, ListRecyclerAdapter.ViewHolder>(NoteDiffCallback) {

    class ViewHolder(
        itemView: View,
        private val onItemClick: (note: Note) -> Unit,
        private val onItemDelete: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemNoteBinding.bind(itemView)

        fun bind(note: Note) =
            binding.run {
                header.text = note.header
                content.text = note.content

                if(note.targetDate != null) {
                    targetDate.text = formatDate(note.targetDate)
                } else {
                    targetDate.text = binding.root.context.getString(
                        R.string.text_indefinite_term
                    )
                }

                deleteButton.setOnClickListener {
                    onItemDelete(layoutPosition)
                }

                root.setOnClickListener {
                    onItemClick(note)
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
        onItemClick,
        onItemDelete
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int
    ) = holder.bind(
        getItem(currentPosition)
    )
}