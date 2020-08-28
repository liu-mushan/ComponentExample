package com.liumushan.loginmodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liumushan.coremodule.jump.ParamConsts
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val account = intent.getStringExtra(ParamConsts.LoginParam.KEY_ACCOUNT)
        val pwd = intent.getStringExtra(ParamConsts.LoginParam.KEY_PASSWORD)
        tvContent.text = "LoginAtcivity account: $account password: $pwd"
    }
}