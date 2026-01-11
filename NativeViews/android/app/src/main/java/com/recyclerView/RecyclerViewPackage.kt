package com.recyclerView

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class RecyclerViewPackage: BaseReactPackage() {

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<in Nothing, in Nothing>> {
        return listOf(ReactRecyclerViewManager(reactContext))
    }
    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        when(name){
            ReactRecyclerViewManager.REACT_CLASS -> ReactRecyclerViewManager(reactContext)
        }
        return null
        }


    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactRecyclerViewManager.REACT_CLASS to ReactModuleInfo(
            name = ReactRecyclerViewManager.REACT_CLASS,
            className = ReactRecyclerViewManager.REACT_CLASS,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }
}