package com.liumushan.coremodule


object ComponentLoader {
    private val componentStrArray = arrayOf(
        "com.liumushan.loginmodule.LoginComponent",
        "com.liumushan.tickermodule.TickerComponent"
    )

    fun load() {
        ComponentManager.getInstance().loadComponents(componentStrArray)
    }
}