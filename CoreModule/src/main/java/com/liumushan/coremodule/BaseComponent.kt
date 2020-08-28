package com.liumushan.coremodule

abstract class BaseComponent {
    fun init() {
        // 初始化页面跳转路由
        initJumpRouter()
        initServiceFactory()

        doInit()
    }

    fun doInit() {

    }

    abstract fun initJumpRouter()

    abstract fun initServiceFactory()
}