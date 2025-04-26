package com.example.freemates_android.ui.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.freemates_android.HomeFragment

class GridSpacingDecoration(
    private val context: Context,
    private val spanCount: Int,     // 그리드 열 개수 (예: 2열, 3열)
    private val spacingDp: Int,     // 아이템 간 간격 (예: 8dp)
    private val includeEdge: Boolean = true
) : RecyclerView.ItemDecoration() {

    private val spacingPx = (spacingDp * context.resources.displayMetrics.density).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = spacingPx - column * spacingPx / spanCount
            outRect.right = (column + 1) * spacingPx / spanCount

            if (position < spanCount) {
                outRect.top = spacingPx
            }
            outRect.bottom = spacingPx
        } else {
            outRect.left = column * spacingPx / spanCount
            outRect.right = spacingPx - (column + 1) * spacingPx / spanCount

            if (position >= spanCount) {
                outRect.top = spacingPx
            }
        }
    }
}

