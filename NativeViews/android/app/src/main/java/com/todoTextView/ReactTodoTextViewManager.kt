package com.todoTextView

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TodoTextViewManagerDelegate
import com.facebook.react.viewmanagers.TodoTextViewManagerInterface

class ReactTodoTextViewManager(context: ReactApplicationContext): SimpleViewManager<ReactTodoTextView>(), TodoTextViewManagerInterface<ReactTodoTextView> {
    private val delegate: TodoTextViewManagerDelegate<ReactTodoTextView, ReactTodoTextViewManager> =
        TodoTextViewManagerDelegate(this)

    override fun getDelegate(): ViewManagerDelegate<ReactTodoTextView> = delegate


    override fun createViewInstance(reactContext: ThemedReactContext): ReactTodoTextView {
         return ReactTodoTextView(reactContext)
    }

    companion object {
        const val REACT_CLASS = "TodoTextView"
    }

    override fun getName(): String = REACT_CLASS

    override fun setFontSize(view: ReactTodoTextView?, value: Int) {
         view?.setFontSize(value)
    }

    override fun setColor(view: ReactTodoTextView?, value: String?) {
        if(value != null){
            view?.setColor(value)
        }
    }

    override fun setFontStyle(view: ReactTodoTextView?, value: String?) {
        if(value != null){
            view?.setFontStyle(value)
        }
    }
}