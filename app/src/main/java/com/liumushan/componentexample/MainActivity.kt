package com.liumushan.component

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.liumushan.componentexample.R
import com.liumushan.coremodule.ServiceManager
import com.liumushan.coremodule.jump.ActionCollection
import com.liumushan.coremodule.jump.JumpManager
import com.liumushan.loginmodule.ILoginService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = ServiceManager.getInstance().getService<ILoginService>(ILoginService::class)
        if (service?.isLogin() == true) {
            JumpManager.jump(this, ActionCollection.getLoginJumpUrl("web", "123"))
        } else {
            JumpManager.jump(this, ActionCollection.TARGET_TICKER)
        }
    }
}