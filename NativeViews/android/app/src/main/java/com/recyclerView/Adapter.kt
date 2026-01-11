package com.recyclerView

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import androidx.core.net.toUri
import androidx.core.view.marginTop
import com.nativeviews.R

class Adapter(private val context: Context, private val items: MutableList<TodoTask>): RecyclerView.Adapter<Adapter.ViewHolder>(){
    inner class ViewHolder(view: LinearLayout): RecyclerView.ViewHolder(view){
        val density = view.context.resources.displayMetrics.density
        val sizeInPx = (100 * density).toInt()
        val marginInPx = (20 * density).toInt()
        val imageView: ImageView = ImageView(view.context).apply {
            val params = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
            params.setMargins(0, 0, marginInPx, 0) // left, top, right, bottom
            layoutParams = params
            background = ContextCompat.getDrawable(context, R.drawable.image_bg)
            clipToOutline = true // Important to clip the image to the shape
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        var textView: TextView = TextView(view.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
           textSize = 20f
            setTextColor(0xFFFF0000.toInt())

        }

        init {
            view.orientation = LinearLayout.HORIZONTAL
            view.gravity = Gravity.CENTER_VERTICAL
            view.isClickable = true
            view.isFocusable = true
            view.foreground = ContextCompat.getDrawable(context, R.drawable.bg_ripple)
            view.addView(imageView)
            view.addView(textView)
            view.background = ContextCompat.getDrawable(context, R.drawable.linear_bg)

           }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val density = parent.context.resources.displayMetrics.density
        val paddingDp = 14
        val paddingPx = (paddingDp * density).toInt()
        val layout = LinearLayout(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,  // width
                RecyclerView.LayoutParams.WRAP_CONTENT   // height
            )

            // Set padding (left, top, right, bottom)
            setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
        }
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theTodoObj = items[position]
        holder.textView.text = theTodoObj.taskText
        holder.imageView.setImageURI(theTodoObj.imageUri)
        Log.d("Adapter", "binding position=$position text=${theTodoObj.taskText}")
    }
}