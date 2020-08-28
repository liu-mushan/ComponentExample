package com.liumushan.loginmodule

import com.liumushan.coremodule.BaseComponent
import com.liumushan.coremodule.ServiceManager
import com.liumushan.coremodule.jump.ActionCollection
import com.liumushan.coremodule.jump.JumpManager

class LoginComponent : BaseComponent() {

    override fun initJumpRouter() {
        val map =
            hashMapOf<String, String>(ActionCollection.TARGET_LOGIN to LoginActivity::class.java.name)
        JumpManager.registerRouteMap(map)
    }

    override fun initServiceFactory() {
        ServiceManager.getInstance().registerServiceFactory(LoginServiceFactory())
    }
}