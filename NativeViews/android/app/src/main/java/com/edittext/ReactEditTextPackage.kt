package com.edittext

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class ReactEditTextPackage: BaseReactPackage() {
    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<in Nothing, in Nothing>> {
        return listOf(ReactEditTextViewManager(reactContext))
    }

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        when (name) {
            ReactEditTextViewManager.REACT_CLASS -> ReactEditTextViewManager(reactContext)
        }
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactEditTextViewManager.REACT_CLASS to ReactModuleInfo(
            name = ReactEditTextViewManager.REACT_CLASS,
            className = ReactEditTextViewManager.REACT_CLASS,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }
}