package com.senex.androidlab1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.ListItemUserBinding
import com.senex.androidlab1.models.User
import com.senex.androidlab1.utils.log

class UserRecyclerAdapter(
    private val users: List<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {
    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemUserBinding.bind(itemView)

        fun bind(user: User, onItemClick: (User) -> Unit) =
            binding.run {
                listItemUserImage.setImageResource(
                    user.imageId ?: R.drawable.image_profile_default
                )
                listItemUserNickname.text = user.nickname
                listItemUserStatus.text = user.status
                listItemUserVerificationIcon.visibility =
                    if (user.isVerified) View.VISIBLE else View.INVISIBLE

                root.setOnClickListener {
                    onItemClick(user)
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_user, parent, false)
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int
    ) = holder.bind(
        users[currentPosition],
        onItemClick
    )

    override fun getItemCount() = users.size
}