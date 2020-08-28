package com.liumushan.loginmodule

class LoginService private constructor(): ILoginService {
    companion object {
       fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = LoginService()
    }

    override fun isLogin(): Boolean {
        return true
    }
}