import android.content.Context
import android.net.Uri
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.nativeviews.R

class ReactImageView(context: Context) : AppCompatImageView(context) {
    val widthInPx = dpToPx(120, context)
    val heightInPx = dpToPx(120, context)

    init {
        setImageResource(R.drawable.gallery_photo)
        scaleType = ScaleType.CENTER_CROP

        // Convert dp to px for consistency

        layoutParams = ViewGroup.LayoutParams(widthInPx, heightInPx)

        setOnClickListener {
            emitOnPress()
        }
    }



    private fun dpToPx(dp: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private val layoutRunnable = Runnable {
        measure(
            MeasureSpec.makeMeasureSpec(widthInPx, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(heightInPx, MeasureSpec.EXACTLY)
        )
        layout(left, top, right, bottom)
    }

    override fun requestLayout() {
        super.requestLayout()
        // Post a second layout pass so the ExoPlayer internal views get correct bounds.
        post(layoutRunnable)
    }

    fun setImageUri(uri: Uri){
        setImageURI(uri)
    }

    private fun emitOnPress() {
        val reactContext = context as ReactContext
        val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
        val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)
        val payload =
            Arguments.createMap().apply {
                putBoolean("isClicked", true)
            }
        val event = OnEmitOnPress(surfaceId, id, payload)
        eventDispatcher?.dispatchEvent(event)
    }

    inner class OnEmitOnPress(
        surfaceId: Int,
        viewId: Int,
        private val payload: WritableMap
    ) : Event<OnEmitOnPress>(surfaceId, viewId) {
        override fun getEventName() = "onPressEvent"

        override fun getEventData() = payload
    }
}
