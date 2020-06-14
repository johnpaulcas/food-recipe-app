package com.johnpaulcas.foodrecipe.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by johnpaulcas on 13/06/2020.
 */
class VerticalSpacingItemDecorator(val verticalSpacingHeight: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalSpacingHeight
    }

}