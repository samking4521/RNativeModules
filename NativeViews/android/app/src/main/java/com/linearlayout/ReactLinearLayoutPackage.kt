package com.linearlayout

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager
import com.todoTextView.ReactTodoTextViewManager

class ReactLinearLayoutPackage: BaseReactPackage() {

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<in Nothing, in Nothing>> {
        return listOf(ReactTodoTextViewManager(reactContext))
    }

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        when(name){
            ReactLinearLayoutViewManager.REACT_CLASS -> ReactLinearLayoutViewManager(reactContext)
        }
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactLinearLayoutViewManager.REACT_CLASS to ReactModuleInfo(
            name = ReactLinearLayoutViewManager.REACT_CLASS,
            className = ReactLinearLayoutViewManager.REACT_CLASS,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }
}