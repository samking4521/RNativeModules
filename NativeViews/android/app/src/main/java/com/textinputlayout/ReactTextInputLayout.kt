package com.textinputlayout

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.nativeviews.R

class ReactTextInputLayout(context: Context): TextInputLayout(context) {
    val radius = resources.getDimension(R.dimen.cornerRadius)

    init {
        isHintEnabled = false
        setBoxCornerRadii(radius, radius, radius, radius)
        boxStrokeWidth = 0
        setBoxBackgroundColorResource(R.color.gray)
        boxBackgroundMode = BOX_BACKGROUND_FILLED
        boxStrokeWidthFocused = 0
        endIconMode = END_ICON_CLEAR_TEXT
        startIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_search)

    }

}