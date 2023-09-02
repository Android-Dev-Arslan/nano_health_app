package com.example.nanohealthsuiteapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration(
    private val context: Context,
    private val colorResId: Int,
    private val heightResId: Int
) : DividerItemDecoration(context, RecyclerView.VERTICAL) {

    private val dividerPaint = Paint()

    init {
        val color = ContextCompat.getColor(context, colorResId)
        dividerPaint.color = color
        val height = context.resources.getDimensionPixelSize(heightResId)
        setDrawable(ContextCompat.getDrawable(context, android.R.color.transparent)!!)
        setDividerHeight(height)
    }

    private fun setDividerHeight(height: Int) {
        val metrics = context.resources.displayMetrics
        val dpHeight = height.toFloat() / metrics.density
        setDrawableHeight(dpHeight.toInt())
    }

    private fun setDrawableHeight(height: Int) {
        val drawable = drawable ?: return
        drawable.setBounds(0, 0, 0, height)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + drawable!!.intrinsicHeight

            drawable?.setBounds(left, top, right, bottom)
            drawable?.draw(c)
        }
    }
}
