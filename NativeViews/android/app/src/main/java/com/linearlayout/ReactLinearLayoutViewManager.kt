package com.linearlayout

import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.LinearLayoutManagerDelegate
import com.facebook.react.viewmanagers.LinearLayoutManagerInterface
import com.textinputlayout.ReactTextInputLayout

@ReactModule(name = ReactLinearLayoutViewManager.REACT_CLASS)
class ReactLinearLayoutViewManager(context: ReactApplicationContext):ViewGroupManager<ReactLinearLayout>(), LinearLayoutManagerInterface<ReactLinearLayout>  {

    private val delegate: LinearLayoutManagerDelegate<ReactLinearLayout, ReactLinearLayoutViewManager> =
        LinearLayoutManagerDelegate(this)

    override fun getDelegate(): ViewManagerDelegate<ReactLinearLayout> = delegate


    companion object {
        const val REACT_CLASS = "LinearLayout"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): ReactLinearLayout = ReactLinearLayout(reactContext)

    override fun getName(): String = REACT_CLASS


    override fun addView(parent: ReactLinearLayout, child: View, index: Int) {
        parent.addView(child, index)
    }

    override fun getChildAt(parent: ReactLinearLayout, index: Int): View {
        return parent.getChildAt(index)
    }

    override fun getChildCount(parent: ReactLinearLayout): Int {
        return parent.childCount
    }

    override fun removeViewAt(parent: ReactLinearLayout, index: Int) {
        parent.removeViewAt(index)
    }

}
