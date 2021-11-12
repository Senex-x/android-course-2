package com.senex.androidlab1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ListItemBinding
import com.senex.androidlab1.models.User

class ListRecyclerAdapter(
    private val users: List<User>,
) : RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)

        fun bind(user: User) =
            binding.run {
                listItemUserNickname.text = user.nickname
                listItemUserStatus.text = user.status
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int
    ) = holder.bind(
        users[currentPosition]
    )

    override fun getItemCount() =
        users.size
}