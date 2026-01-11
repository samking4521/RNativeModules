package com.textview

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.viewmanagers.MyTextViewManagerInterface
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.viewmanagers.MyTextViewManagerDelegate

@ReactModule(name = MyTextViewManager.NAME)
class MyTextViewManager(context: ReactApplicationContext) : SimpleViewManager<MyTextView>(),
    MyTextViewManagerInterface<MyTextView> { // <-- auto-generated interface

    companion object {
        const val NAME = "MyTextView"
    }

    override fun getName() = NAME

    override fun createViewInstance(reactContext: ThemedReactContext): MyTextView {
        return MyTextView(reactContext)
    }

    // This comes from the generated interface (from your spec)
    override fun setCustomText(view: MyTextView, value: String?) {
        view.setCustomText(value)
    }

    override fun getDelegate(): MyTextViewManagerDelegate<MyTextView, MyTextViewManager> {
        return MyTextViewManagerDelegate(this)
    }

    override fun setFontSize(view: MyTextView?, value: Float) {
            view?.setFontSize(value)
    }

    override fun setFontWeight(view: MyTextView?, value: String?) {
           view?.setFontWeight(value)
    }

    override fun setTextAlign(view: MyTextView?, value: String?) {
         view?.setTextAlign(value)
    }

    override fun setColor(view: MyTextView?, value: String?) {
        view?.setColor(value)
    }

    // 🔹 Register the "onPress" event here
    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
        return mapOf(
            "onPress" to mapOf("registrationName" to "onPress")
        )
    }

    override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
        mapOf(
            "onLongPress" to
                    mapOf(
                        "phasedRegistrationNames" to
                                mapOf(
                                    "bubbled" to "onLongPress",
                                    "captured" to "onLongPressCapture"
                                )))
}
