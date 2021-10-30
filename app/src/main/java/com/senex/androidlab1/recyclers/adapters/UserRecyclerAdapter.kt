package com.senex.androidlab1.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.databinding.ListItemUserBinding
import com.senex.androidlab1.models.User

class UserRecyclerAdapter(
    private val data: List<User>
) : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListItemUserBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemUserBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).root
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = data[position]

        holder.binding.initViews(currentUser)
    }

    private fun ListItemUserBinding.initViews(currentUser: User) {
        listItemUserNickname.text = currentUser.nickname
    }

    override fun getItemCount(): Int {
        return data.size
    }
}