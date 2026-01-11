package com.recyclerView

import android.util.Log
import androidx.core.net.toUri
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.RecyclerViewManagerDelegate
import com.facebook.react.viewmanagers.RecyclerViewManagerInterface

class ReactRecyclerViewManager(context: ReactApplicationContext): SimpleViewManager<ReactRecyclerView>(), RecyclerViewManagerInterface<ReactRecyclerView> {
    override fun createViewInstance(reactContext: ThemedReactContext): ReactRecyclerView {
        return ReactRecyclerView(reactContext)
    }

    private val delegate: RecyclerViewManagerDelegate<ReactRecyclerView, ReactRecyclerViewManager> =
        RecyclerViewManagerDelegate(this)

    override fun getDelegate(): ViewManagerDelegate<ReactRecyclerView> = delegate


    override fun getName(): String {
       return REACT_CLASS
    }

    override fun setItems(view: ReactRecyclerView?, value: ReadableArray?) {
        if(value != null){
            val taskList = mutableListOf<TodoTask>()

            for (i in 0 until value.size()) {
                val map = value.getMap(i)  // Each element is a ReadableMap
                if (map != null) {
                    val imageUri = map.getString("uri")?.toUri() ?: "".toUri()
                    val text = map.getString("text") ?: ""
                    taskList.add(TodoTask(imageUri, text))
                }
            }
            Log.d("theList", taskList.size.toString())
            // Pass list to your RecyclerView
            view?.setItems(taskList)
        }
    }


    companion object {
        const val REACT_CLASS = "RecyclerView"
    }
}