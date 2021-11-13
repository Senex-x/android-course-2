package com.senex.androidlab1.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.senex.androidlab1.models.User

object UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}