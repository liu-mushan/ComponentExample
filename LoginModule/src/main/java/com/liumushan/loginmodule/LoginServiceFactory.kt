package com.liumushan.loginmodule

import com.liumushan.coremodule.IService
import com.liumushan.coremodule.IServiceFactory
import com.liumushan.coremodule.SupportServiceList
import kotlin.reflect.KClass


@SupportServiceList(ILoginService::class)
class LoginServiceFactory : IServiceFactory {
    override fun createService(clazz: KClass<out IService>): IService? {
        if (clazz == ILoginService::class) {
           return LoginService.getInstance()
        }
        return null
    }
}