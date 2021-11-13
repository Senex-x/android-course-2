package com.senex.androidlab1.adapters.swipeable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.databinding.SwipeableListItemBinding
import com.senex.androidlab1.models.User
import com.senex.androidlab1.utils.generateImageResources
import kotlin.random.Random

class SwipeableRecyclerAdapter(
    private val users: List<User>,
) : RecyclerView.Adapter<SwipeableRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: SwipeableListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) =
            binding.run {
                listItemUserNickname.text = user.name
                listItemUserDescription.text = user.description
                listItemUserPager.adapter = SwipeablePagerAdapter(
                    generateImageResources(Random.nextInt(3, 6))
                )
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        SwipeableListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
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