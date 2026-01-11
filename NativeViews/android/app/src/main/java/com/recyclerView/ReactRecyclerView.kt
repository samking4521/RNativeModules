package com.recyclerView

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReactRecyclerView(context: Context): RecyclerView(context) {
    private var todoTaskArr: MutableList<TodoTask> = mutableListOf()
    private var todoAdapter: com.recyclerView.Adapter
      init {
          layoutManager = LinearLayoutManager(context)
          todoAdapter = Adapter(context, todoTaskArr)
          this.adapter = todoAdapter

          // Set default width & height for the RecyclerView itself
          layoutParams = LayoutParams(
              LayoutParams.MATCH_PARENT, // or a fixed size in px
              LayoutParams.WRAP_CONTENT,// example fixed height in px
          )
      }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthSpec)
        var totalHeight = 0

        // Measure each child (simplified)
        for (i in 0 until adapter!!.itemCount) {
            val holder = adapter!!.createViewHolder(this, adapter!!.getItemViewType(i))
            adapter!!.onBindViewHolder(holder, i)
            holder.itemView.measure(
                MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.EXACTLY),
                MeasureSpec.UNSPECIFIED
            )
            totalHeight += holder.itemView.measuredHeight
        }

        val finalHeight = totalHeight.coerceAtMost(MeasureSpec.getSize(heightSpec))
        setMeasuredDimension(desiredWidth, finalHeight)
    }

    fun setItems(newArr: MutableList<TodoTask>){
        Log.d("Tesla", "This is called")
            todoTaskArr.clear()
            todoTaskArr.addAll(newArr)
            todoAdapter.notifyDataSetChanged()
    }
}