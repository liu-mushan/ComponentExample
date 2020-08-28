package com.liumushan.coremodule.jump

import android.text.TextUtils
import java.net.URLDecoder
import java.util.*

object JumpParser {
    private const val TAG = "JumpParser"

    /**
     * 跳转scheme
     */
    private const val URL_SEPARATE_SYMBOL = "?"

    fun parseAction(source: String, jumpUrl: String?): JumpAction? {
        var jumpAction: JumpAction? = null
        if (!TextUtils.isEmpty(jumpUrl)) {
            jumpAction = parse(source, jumpUrl)
        }
        return jumpAction
    }

    /**
     * 将协议字符串解析为JumpAction实例
     *
     * @param jumpUrl 跳转url
     * @return null表示解析失败
     */
    private fun parse(source: String, jumpUrl: String?): JumpAction? {
        if (jumpUrl == null) {
            return null
        }
        val jumpAction = JumpAction()
        jumpAction.jumpUrl = jumpUrl
        jumpAction.sourcePage = source
        var proto: String? = null
        var strParams: String? = null
        if (jumpUrl.contains("?")) {
            val temp = jumpUrl.split("?").toTypedArray()
            if (temp.size == 2) {
                proto = temp[0]
                strParams = temp[1]
            }
        } else {
            proto = jumpUrl
        }

        //解析协议、地址字段
        if (!TextUtils.isEmpty(proto) && proto!!.contains("://")) {
            val temp = proto.split("://").toTypedArray()
            if (temp.size == 2) {
                jumpAction.schema = temp[0]
                jumpAction.destination = temp[1]
            }
        }

        //如果url中未带有协议字段，则整个proto就视为Destination页
        if (TextUtils.isEmpty(jumpAction.destination)) {
            jumpAction.destination = proto
        }
        parseJumpType(jumpAction)

        //解析参数，只有本地跳转才需要解析后面的参数
        if (!TextUtils.isEmpty(strParams) && jumpAction.jumpType === JumpAction.JumpType.TYPE_NATIVE_JUMP) {
            val params = strParams!!.split("&").toTypedArray()
            if (params.isNotEmpty()) {
                val paramMap = HashMap<String?, String?>()
                for (i in params.indices) {
                    val index = params[i].indexOf("=")
                    if (index > 0 && index < params[i].length) {
                        val paramName = params[i].substring(0, index)
                        val value =
                            params[i].substring(index + 1, params[i].length)
                        if (!TextUtils.isEmpty(paramName) && !TextUtils.isEmpty(value)) {
                            paramMap[paramName] = URLDecoder.decode(value)
                        }
                    }
                }
                jumpAction.setParamMap(paramMap)
            }
        }
        return jumpAction
    }

    private fun parseJumpType(jumpAction: JumpAction) {
        jumpAction.jumpType = JumpAction.JumpType.TYPE_NATIVE_JUMP
    }
}