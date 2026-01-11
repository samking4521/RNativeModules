package com.textview

import com.facebook.react.BaseReactPackage
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider


class ReactTextViewPackage : BaseReactPackage() {
    override fun createViewManagers(reactContext: ReactApplicationContext)
            = listOf(MyTextViewManager(reactContext))


    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
       when(name){
           MyTextViewManager.NAME -> MyTextViewManager(reactContext)
       }
        return null
       }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(MyTextViewManager.NAME to ReactModuleInfo(
            name = MyTextViewManager.NAME,
            className = MyTextViewManager.NAME,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }


}
