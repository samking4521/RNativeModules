package com.nativeimagepicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.myapp.NativeImagePickerSpec
import com.nativeviews.MainActivity

//class NativeImagePickerModule(
//    reactContext: ReactApplicationContext
//) : NativeImagePickerSpec(reactContext), ActivityEventListener {
//
//    private var pendingPromise: Promise? = null
//    private val REQUEST_CODE_PICK_IMAGE = 1001
//
//    init {
//        reactContext.addActivityEventListener(this)
//    }
//
//    override fun getName() = NAME
//
//    override fun getImageUri(promise: Promise?) {
//        val activity = reactApplicationContext.currentActivity
//        if (activity == null) {
//            promise?.reject("E_NO_ACTIVITY", "No current activity")
//            return
//        }
//
//        Log.d("pickImage", "called")
//        pendingPromise = promise
//
//        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "image/*"
//        }
//        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
//    }
//
//    // 🔹 Called when an activity you started finishes
//    override fun onActivityResult(
//        activity: Activity,
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//            val uri: Uri? = data?.data
//            Log.d("pickImage", "uri: $uri")
//            pendingPromise?.resolve(uri?.toString())
//            pendingPromise = null
//        }
//    }
//
//    // 🔹 Required override but not used here
//    override fun onNewIntent(intent: Intent) {}
//
//    companion object {
//        const val NAME = "NativeImagePicker"
//    }
//}



//class NativeImagePickerModule(reactContext: ReactApplicationContext)
//    : NativeImagePickerSpec(reactContext) {
//
//
//    override fun getName() = NAME
//
//
//
//    companion object {
//        const val NAME = "NativeImagePicker"
//        private var promise: Promise? = null
//        fun handleResult(uri: Uri?) {
//            if (uri != null) {
//                val theUri = uri.toString()
//                promise?.resolve(theUri)
//            } else {
//                promise?.reject("CANCELLED", "User cancelled")
//            }
//            promise = null
//        }
//
//    }
//
//
//    override fun getImageUri(promise: Promise?) {
//        Companion.promise = promise
//        (reactApplicationContext.currentActivity as? MainActivity)?.pickImageLauncher?.launch("image/*")
//            ?: promise?.reject("NO_ACTIVITY", "No current activity")
//
//    }
//}

//class NativeImagePickerModule(reactContext: ReactApplicationContext): NativeImagePickerSpec(reactContext), ActivityEventListener{
//
//    private var promise: Promise? = null
//    val REQUEST_CODE = 1000
//
//    override fun getName(): String = NAME
//
//    companion object {
//        const val NAME = "NativeImagePicker"
//    }
//
//    init {
//        reactContext.addActivityEventListener(this)
//    }
//
//    override fun getImageUri(promise: Promise?) {
//        val activity = reactApplicationContext.currentActivity
//        if(activity == null){
//            promise?.reject("REJECT", "No activity found")
//            return
//        }
//        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "image/*"
//        }
//        this.promise = promise
//        activity.startActivityForResult(intent, REQUEST_CODE)
//
//    }
//
//    override fun onActivityResult(
//        activity: Activity,
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
//               val uri = data?.data
//               if(uri != null){
//                   promise?.resolve(uri.toString())
//               }else{
//                   promise?.reject("CANCELLED", "User selected nothing")
//               }
//        }
//    }
//
//    override fun onNewIntent(intent: Intent) {}
//
//}

class NativeImagePickerModule(reactContext: ReactApplicationContext): NativeImagePickerSpec(reactContext){

    override fun getName(): String = NAME

    companion object {
         const val NAME = "NativeImagePicker"
         var promise: Promise? = null
         fun handleResult(uri: Uri){
             val theUri = uri.toString()
            promise?.resolve(theUri)
        }
    }

    override fun getImageUri(promise: Promise?) {
         val activity = reactApplicationContext.currentActivity
        if(activity == null){
            promise?.reject("NO ACTIVITY", "Activity not created yet")
            return
        }
        Companion.promise = promise
        (activity as? MainActivity)?.imageLauncher?.launch("image/*") ?: promise?.reject("CANCELLED", "CANCELLED LAUNCH ACTIVITY")


    }

}




