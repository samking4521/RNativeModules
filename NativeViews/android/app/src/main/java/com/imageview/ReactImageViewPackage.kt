package com.imageview

import ReactImageViewManager
import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class ReactImageViewPackage : BaseReactPackage() {

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(ReactImageViewManager(reactContext))
    }

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        when (name) {
            ReactImageViewManager.REACT_CLASS -> ReactImageViewManager(reactContext)
        }
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactImageViewManager.REACT_CLASS to ReactModuleInfo(
            name = ReactImageViewManager.REACT_CLASS,
            className = ReactImageViewManager.REACT_CLASS,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
         )
        )
    }

}
