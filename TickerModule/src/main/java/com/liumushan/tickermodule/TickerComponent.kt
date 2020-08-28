package com.liumushan.tickermodule

import com.liumushan.coremodule.BaseComponent
import com.liumushan.coremodule.jump.ActionCollection
import com.liumushan.coremodule.jump.JumpManager

class TickerComponent : BaseComponent() {
    override fun initJumpRouter() {
        val map =
            hashMapOf<String, String>(ActionCollection.TARGET_TICKER to TickerActivity::class.java.name)
        JumpManager.registerRouteMap(map)
    }

    override fun initServiceFactory() {

    }
}