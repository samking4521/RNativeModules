package com.edittext

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.EditTextManagerDelegate
import com.facebook.react.viewmanagers.EditTextManagerInterface

@ReactModule(name = ReactEditTextViewManager.REACT_CLASS)
class ReactEditTextViewManager(context: ReactApplicationContext): SimpleViewManager<ReactEditText>(), EditTextManagerInterface<ReactEditText> {
    companion object {
        const val REACT_CLASS = "EditText"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): ReactEditText {
        return ReactEditText(reactContext)
    }

    override fun getName(): String = REACT_CLASS

    private val delegate: EditTextManagerDelegate<ReactEditText, ReactEditTextViewManager> =
        EditTextManagerDelegate(this)

    override fun getDelegate(): ViewManagerDelegate<ReactEditText> = delegate

    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> =
        mapOf(
            "onChangeText" to mapOf(
                "registrationName" to "onChangeText"
            )
        )



}