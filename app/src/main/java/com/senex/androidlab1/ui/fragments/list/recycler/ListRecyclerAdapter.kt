package com.senex.androidlab1.ui.fragments.list.recycler

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
    private val onItemClick: (note: Note) -> Unit
) : ListAdapter<Note, ListRecyclerAdapter.ViewHolder>(NoteDiffCallback) {

    class ViewHolder(
        itemView: View,
        private val onItemClick: (note: Note) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemNoteBinding.bind(itemView)

        fun bind(note: Note) =
            binding.run {
                header.text = note.header
                content.text = note.content
                openingDate.text = formatDate(note.openingDate)
                targetDate.text = if (note.targetDate != null)
                    formatDate(note.targetDate)
                else
                    binding.root.context.getString(
                        R.string.text_indefinite
                    )
                longitude.text = if(note.longitude != null)
                    note.longitude.toString() else "Not set"
                latitude.text =if(note.latitude != null)
                    note.latitude.toString() else "Not set"

                root.setOnClickListener {
                    onItemClick(note)
                }

                if(note.content.isEmpty()) {
                    content.visibility = View.GONE
                }
                if(note.latitude == null || note.longitude == null) {

                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_note, parent, false),
        onItemClick
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int,
    ) = holder.bind(
        getItem(currentPosition)
    )
}