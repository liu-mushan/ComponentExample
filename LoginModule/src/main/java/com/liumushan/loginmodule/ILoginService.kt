package com.liumushan.loginmodule

import com.liumushan.coremodule.IService

interface ILoginService : IService {
    fun isLogin(): Boolean
}