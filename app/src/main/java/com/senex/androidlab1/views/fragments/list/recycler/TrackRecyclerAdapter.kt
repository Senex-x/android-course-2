package com.senex.androidlab1.views.fragments.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senex.androidlab1.databinding.ListItemMusicTrackBinding
import com.senex.androidlab1.models.Track

class TrackRecyclerAdapter(
    private val tracks: List<Track>,
    private val onItemClick: (Long) -> Unit,
) : RecyclerView.Adapter<TrackRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListItemMusicTrackBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track, onItemClick: (Long) -> Unit) =
            binding.run {

                root.setOnClickListener {
                    onItemClick(track.id)
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ViewHolder(
        ListItemMusicTrackBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        currentPosition: Int,
    ) = holder.bind(
        tracks[currentPosition],
        onItemClick
    )

    override fun getItemCount() = tracks.size
}