package com.textinputlayout

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class ReactTextInputLayoutPackage: BaseReactPackage() {
    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<in Nothing, in Nothing>> {
         return listOf(ReactTextInputLayoutViewManager(reactContext))
    }

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        when (name) {
            ReactTextInputLayoutViewManager.REACT_CLASS -> ReactTextInputLayoutViewManager(reactContext)
        }
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactTextInputLayoutViewManager.REACT_CLASS to ReactModuleInfo(
            name = ReactTextInputLayoutViewManager.REACT_CLASS,
            className = ReactTextInputLayoutViewManager.REACT_CLASS,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }
}