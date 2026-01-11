package com.todoTextView

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.toColorInt

class ReactTodoTextView(context: Context): AppCompatTextView(context) {

   private fun dpToPx(dp: Int): Float {
      return dp * resources.displayMetrics.density
   }

   private fun spToPx(sp: Int): Float {
      return sp * resources.displayMetrics.scaledDensity
   }

   fun setFontSize(value: Int){
        val newValue = spToPx(value)
        textSize = newValue
   }

   fun setColor(value: String){
         val color = value.toColorInt()
         setTextColor(color)
   }

   fun setFontStyle(value: String){
         when(value){
            "bold" -> {
               setTypeface(null, Typeface.BOLD)
            }
            "italic" -> {
               setTypeface(null, Typeface.ITALIC)
            }
            "normal" -> {
               setTypeface(null, Typeface.NORMAL)
            }
         }
   }



}