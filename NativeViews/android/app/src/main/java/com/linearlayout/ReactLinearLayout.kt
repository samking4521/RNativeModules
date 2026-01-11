package com.linearlayout

import android.content.Context
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.nativeviews.R

class ReactLinearLayout(context: Context): LinearLayout(context) {
    init {
        orientation = HORIZONTAL
        val scale = resources.displayMetrics.density // 1 dp = scale px
        val paddingInPx = (20 * scale).toInt()
        setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)
        background = ContextCompat.getDrawable(context, R.drawable.bg_ripple)
    }
}