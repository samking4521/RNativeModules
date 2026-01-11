import android.net.Uri
import android.view.ViewGroup
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.ImageViewsManagerDelegate
import com.facebook.react.viewmanagers.ImageViewsManagerInterface
import androidx.core.net.toUri
import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.ReactShadowNode

@ReactModule(name = ReactImageViewManager.REACT_CLASS)
class ReactImageViewManager(context: ReactApplicationContext) : SimpleViewManager<ReactImageView>(), ImageViewsManagerInterface<ReactImageView> {
    private val delegate: ImageViewsManagerDelegate<ReactImageView, ReactImageViewManager> =
        ImageViewsManagerDelegate(this)

    override fun getDelegate(): ViewManagerDelegate<ReactImageView> = delegate

    override fun getName(): String = REACT_CLASS

    override fun createViewInstance(context: ThemedReactContext): ReactImageView {
            return ReactImageView(context)
    }

    companion object {
        const val REACT_CLASS = "ImageViews"
    }

    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> =
        mapOf(
            "onPressEvent" to mapOf(
                "registrationName" to "onPressEvent"
            )
        )



    override fun setSource(view: ReactImageView?, value: String?) {
        if(value != null){
            val uri = value.toUri()
            view?.setImageURI(uri)
        }
    }


}