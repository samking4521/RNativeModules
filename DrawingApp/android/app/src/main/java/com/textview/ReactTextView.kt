package com.textview
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.toColorInt
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import kotlin.random.Random

class MyTextView(
    context: Context,
) : AppCompatTextView(context) {
      init {
          setOnClickListener {
              val a = Random.nextInt(256)
              val r = Random.nextInt(256)
              val g = Random.nextInt(256)
              emitOnPress(a, r, g)
          }

          setOnLongClickListener{
              emitLongClick()
          }
      }

    private fun emitLongClick(): Boolean{
        val reactContext = context as ReactContext
        val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
        val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)

        val payload = Arguments.createMap().apply {
            putBoolean("isClicked", true)
        }

        val event = OnLongPressEvent(surfaceId, id, payload)
        eventDispatcher?.dispatchEvent(event)
        return true
    }

    private fun emitOnPress(x: Int, y: Int, z: Int) {
        val reactContext = context as ReactContext
        val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
        val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)

        val payload = Arguments.createMap().apply {
            putInt("x", x)
            putInt("y", y)
            putInt("z", z)
        }

        val event = OnPressEvent(surfaceId, id, payload)
        eventDispatcher?.dispatchEvent(event)
    }

    inner class OnPressEvent(
        surfaceId: Int,
        viewId: Int,
        private val payload: WritableMap
    ) : Event<OnPressEvent>(surfaceId, viewId) {
        override fun getEventName() = "onPress"
        override fun getEventData() = payload
    }

    inner class OnLongPressEvent(
        surfaceId: Int,
        viewId: Int,
        private val payload: WritableMap
    ) : Event<OnPressEvent>(surfaceId, viewId) {
        override fun getEventName() = "onLongPress"
        override fun getEventData() = payload
    }

    fun setCustomText(value: String?) {
        text = value ?: "Default from XML"
    }

     fun setFontSize(value: Float) {
          textSize = value
    }

     fun setFontWeight(value: String?) {
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

     fun setTextAlign(value: String?) {
         when(value){
             "left" -> {
                 gravity = Gravity.START
             }
             "right" -> {
                gravity = Gravity.END
             }
             "center" -> {
                gravity = Gravity.CENTER
             }
         }
    }

     fun setColor(value: String?) {
          val colorVal = value?.toColorInt()  // returns Int
          setTextColor(colorVal!!)
    }

}
