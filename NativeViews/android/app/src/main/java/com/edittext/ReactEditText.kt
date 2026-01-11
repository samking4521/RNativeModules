package com.edittext

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.google.android.material.textfield.TextInputEditText
import com.nativeviews.R

class ReactEditText(context: Context): TextInputEditText(context) {
    init {
        hint = "Enter a task"
        background = null
        inputType = InputType.TYPE_CLASS_TEXT
        setTextColor(ContextCompat.getColor(context, R.color.colorText))
        imeOptions = EditorInfo.IME_ACTION_SEARCH
        isSingleLine = false

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                  emitOnTextChanged(p0.toString())
            }

        })
    }


    fun emitOnTextChanged(theText: String) {
        val reactContext = context as ReactContext
        val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
        val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)
        val payload =
            Arguments.createMap().apply {
                putString("text", theText)
            }
        val event = OnEmitOnTextChanged(surfaceId, id, payload)

        eventDispatcher?.dispatchEvent(event)
    }


    inner class OnEmitOnTextChanged(
        surfaceId: Int,
        viewId: Int,
        private val payload: WritableMap
    ) : Event<OnEmitOnTextChanged>(surfaceId, viewId) {
        override fun getEventName() = "onChangeText"

        override fun getEventData() = payload
    }
}