package com.example.freemates_android.ui.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingDecoration(
    private val context: Context,
    private val spacingDp: Int
) : RecyclerView.ItemDecoration() {

    private val spacingPx = (spacingDp * context.resources.displayMetrics.density).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = spacingPx
    }
}
