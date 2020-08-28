package com.liumushan.coremodule.jump

import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

object ActionCollection {
    const val TARGET_LOGIN = "loginActivity"
    const val TARGET_TICKER = "tickerActivity"
    fun getLoginJumpUrl(account: String, pwd: String): String {
        val hashMap = HashMap<String, String>()
        hashMap[ParamConsts.LoginParam.KEY_ACCOUNT] = account
        hashMap[ParamConsts.LoginParam.KEY_PASSWORD] = pwd
        return createUrl(TARGET_LOGIN, hashMap)
    }

    private fun createUrl(schema: String, params: Map<String, String>?): String {
        return schema + createParasms(params)
    }

    /**
     * 创建url的参数
     */
    private fun createParasms(params: Map<String, String>?): String {
        if (params == null || params.isEmpty()) {
            return ""
        }
        val list: MutableList<String> = ArrayList()
        val entries = params.entries
        for ((key, value) in entries) {
            try {
                list.add(appendParam(key, URLEncoder.encode(value, "UTF-8")))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        val sb = StringBuilder()
        for (i in list.indices) {
            val param = list[i]
            if (!TextUtils.isEmpty(param)) {
                if (i != 0) {
                    sb.append("&")
                }
                sb.append(param)
            }
        }
        var param = sb.toString()
        if (!TextUtils.isEmpty(param)) {
            param = "?$param"
        }
        return param
    }

    private fun appendParam(name: String, value: String?): String {
        return if (!TextUtils.isEmpty(value)) "$name=$value" else ""
    }
}