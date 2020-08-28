/**
 * Copyright (C) 2010-2012 TENCENT Inc.All Rights Reserved.
 * FileName: JumpParams.java
 * Description:
 * CreatTime: 2013-11-29
 *
 * @author dancycai
 */
package com.liumushan.coremodule.jump

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import java.io.Serializable
import java.util.*

class JumpAction : Serializable {
    var jumpType: JumpType? = null  //跳转类型，分为native和H5

    enum class JumpType {
        TYPE_NATIVE_JUMP, TYPE_H5_JUMP
    }

    var schema: String? = null //跳转协议
    var sourcePage: String? = null
    var destination: String? = null //跳转目的地，native跳转专用
    private var paramMap: HashMap<String?, String?>? =
        HashMap() //跳转参数，native跳转专用
    var jumpUrl: String? = null //跳转URL
    private val checkLogin = false
    var actionReportName: String? = null

    val isValid: Boolean
        get() = !TextUtils.isEmpty(sourcePage) && !TextUtils.isEmpty(destination)

    val firstDestination: String?
        get() {
            val split = destination?.split("/")?.toTypedArray() ?: return ""
            return if (split.isNotEmpty()) {
                split[0]
            } else destination
        }

    fun getParamMap(): HashMap<String?, String?> {
        if (paramMap == null) {
            paramMap = HashMap()
        }
        return paramMap!!
    }

    fun putParamsToBundle(
        intent: Intent?,
        params: HashMap<String?, String?>?
    ) {
        if (intent != null && params != null && !params.isEmpty()) {
            for ((key, value) in params) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    intent.putExtra(key, value)
                }
            }
        }
    }

    fun setParamMap(paramMap: HashMap<String?, String?>?) {
        this.paramMap = paramMap
    }

    fun addParams(paramMap: HashMap<String?, String?>?) {
        if (paramMap != null && !paramMap.isEmpty()) {
            this.paramMap!!.putAll(paramMap)
        }
    }

    fun getParamValueByKey(key: String?): String? {
        var ret: String? = null
        if (paramMap != null) {
            ret = paramMap!![key]
        }
        return ret
    }
}