package com.todoTextView

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class ReactTodoTextViewPackage: BaseReactPackage() {

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<in Nothing, in Nothing>> {
        return listOf(ReactTodoTextViewManager(reactContext))
    }

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        when(name){
            ReactTodoTextViewManager.REACT_CLASS -> ReactTodoTextViewManager(reactContext)
        }
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactTodoTextViewManager.REACT_CLASS to ReactModuleInfo(
            name = ReactTodoTextViewManager.REACT_CLASS,
            className = ReactTodoTextViewManager.REACT_CLASS,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }

}