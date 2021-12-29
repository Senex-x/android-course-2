package com.senex.androidlab1.views.fragments.list.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val margin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // Adapter should always present
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = margin
        }
        outRect.bottom = margin
    }
}