package com.liumushan.component

import android.app.Application
import com.liumushan.coremodule.ComponentLoader
import com.liumushan.coremodule.ComponentManager

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ComponentLoader.load()
        ComponentManager.getInstance().init()
    }
}