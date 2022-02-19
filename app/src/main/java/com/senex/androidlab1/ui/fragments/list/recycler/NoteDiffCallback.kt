package com.senex.androidlab1.ui.fragments.list.recycler

import androidx.recyclerview.widget.DiffUtil
import com.senex.androidlab1.models.Note

object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}