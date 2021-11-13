package com.senex.androidlab1.adapters.swipeable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.R
import com.senex.androidlab1.databinding.PageItemBinding

class SwipeablePagerAdapter(
    private val imageResources: List<Int>
) : RecyclerView.Adapter<SwipeablePagerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PageItemBinding.bind(itemView)

        fun bind(imageResource: Int) {
            binding.pageImage.setImageResource(imageResource)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.page_item, parent, false)
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = holder.bind(
        imageResources[position]
    )

    override fun getItemCount() =
        imageResources.size
}
