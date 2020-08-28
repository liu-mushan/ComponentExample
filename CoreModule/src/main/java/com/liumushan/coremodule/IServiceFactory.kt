package com.liumushan.coremodule

import kotlin.reflect.KClass

interface IServiceFactory {
    fun createService(clazz: KClass<out IService>): IService?
}