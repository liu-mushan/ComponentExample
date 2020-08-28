package com.liumushan.coremodule

class ComponentManager private constructor() {
    private val components by lazy { arrayListOf<BaseComponent>() }

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = ComponentManager()
    }

    fun init() {
        for (component in components) {
            component.init()
        }
    }

    fun loadComponents(componentClassArray: Array<String>) {
        for (componentClassName in componentClassArray) {
            createComponent(componentClassName)
        }
    }

    private fun createComponent(componentClassName: String) {
        try {
            val component =
                Class.forName(componentClassName).newInstance() as? BaseComponent ?: return
            components.add(component)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}