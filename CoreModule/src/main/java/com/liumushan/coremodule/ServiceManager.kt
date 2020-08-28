package com.liumushan.coremodule


import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class ServiceManager private constructor() {
    private val serviceFactoryMap: MutableMap<KClass<out IService>, IServiceFactory> =
        ConcurrentHashMap()

    fun registerServiceFactory(serviceFactory: IServiceFactory?) {
        if (serviceFactory == null) {
            return
        }
        val supportServiceList = serviceFactory.javaClass.getAnnotation(
            SupportServiceList::class.java
        )
        if (supportServiceList != null) {
            val supportClasses = supportServiceList.value
            if (supportClasses.isNotEmpty()) {
                for (supportClass in supportClasses) {
                    serviceFactoryMap[supportClass] = serviceFactory
                }
            }
        }
    }

    fun <T : IService> getService(serviceInterface: KClass<out IService>): T? {
        val serviceFactory = serviceFactoryMap[serviceInterface]
        try {
            return serviceFactory?.createService(serviceInterface) as? T?
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = ServiceManager()
    }
}