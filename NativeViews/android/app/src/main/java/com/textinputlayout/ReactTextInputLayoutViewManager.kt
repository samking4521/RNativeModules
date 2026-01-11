package com.textinputlayout

import android.view.View
import com.edittext.ReactEditText
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TextInputLayoutManagerDelegate
import com.facebook.react.viewmanagers.TextInputLayoutManagerInterface

@ReactModule(name = ReactTextInputLayoutViewManager.REACT_CLASS)
class ReactTextInputLayoutViewManager(context: ReactApplicationContext): ViewGroupManager<ReactTextInputLayout>(), TextInputLayoutManagerInterface<ReactTextInputLayout> {
    companion object {
        const val REACT_CLASS = "TextInputLayout"
    }
    private val delegate: TextInputLayoutManagerDelegate<ReactTextInputLayout, ReactTextInputLayoutViewManager> =
        TextInputLayoutManagerDelegate(this)

    override fun getDelegate(): ViewManagerDelegate<ReactTextInputLayout> = delegate

    override fun createViewInstance(reactContext: ThemedReactContext): ReactTextInputLayout {
         return ReactTextInputLayout(reactContext)
    }

    override fun getName(): String = REACT_CLASS

    // This is what allows <EditText /> to be a child
    override fun addView(parent: ReactTextInputLayout, child: View, index: Int) {
        parent.addView(child, index)
    }

    override fun getChildAt(parent: ReactTextInputLayout, index: Int): View {
        return parent.getChildAt(index)
    }

    override fun getChildCount(parent: ReactTextInputLayout): Int {
        return parent.childCount
    }

    override fun removeViewAt(parent: ReactTextInputLayout, index: Int) {
        parent.removeViewAt(index)
    }

}