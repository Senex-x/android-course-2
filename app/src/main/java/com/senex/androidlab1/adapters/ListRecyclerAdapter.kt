package com.senex.androidlab1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.adapters.diffutils.UserDiffCallback
import com.senex.androidlab1.databinding.ListItemBinding
import com.senex.androidlab1.models.User

class ListRecyclerAdapter(
    private val onItemDelete: (position: Int) -> Unit
) : ListAdapter<User, ListRecyclerAdapter.ViewHolder>(UserDiffCallback) {

    class ViewHolder(
        itemView: View,
        private val onItemDelete: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)

        fun bind(user: User) =
            binding.run {
                listItemUserNickname.text = user.name
                listItemUserDescription.text = user.description
                listItemUserDelete.setOnClickListener {
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
            .inflate(R.layout.list_item, parent, false),
        onItemDelete
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int
    ) = holder.bind(
        getItem(currentPosition)
    )
}