package com.liumushan.coremodule.jump

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import java.util.*

/**
 * Created by duzhixiong on 2017/7/17.
 */
object JumpManager {
    private const val TAG = "JumpManager"
    private val mJumpTable =
        HashMap<String, String>(512) // 跳转映射表,K:JumpAction.destination字段，V：目标activity的classname

    /**
     * 防止重复点击
     */
    private const val MIN_JUMP_DELAY_TIME = 800
    private val mJumpTimeMap =
        HashMap<String, Long>()

    fun registerRouteMap(jumpTable: HashMap<String, String>?) {
        if (jumpTable != null && jumpTable.size > 0) {
            mJumpTable.putAll(jumpTable)
        }
    }

    fun jump(
        sourceActivity: Context?,
        url: String,
        flag: Int
    ): Boolean {
        return jump(sourceActivity, url, null, false, -1, flag)
    }

    fun jump(sourceActivity: Context?, url: String): Boolean {
        return jump(sourceActivity, url, null, false, -1, -1)
    }

    fun jump(
        sourceActivity: Context?,
        url: String,
        extraParams: HashMap<String?, String?>?
    ): Boolean {
        return jump(sourceActivity, url, extraParams, false, -1, -1)
    }

    fun jumpForResult(
        sourceActivity: Context?,
        url: String,
        requestCode: Int
    ): Boolean {
        return jump(sourceActivity, url, null, true, requestCode, -1)
    }

    //慎用！！！ flag为Intent.FLAG_ACTIVITY_NEW_TASK， forResult会提前回调
    fun jumpForResult(
        sourceActivity: Context?,
        url: String,
        requestCode: Int,
        flag: Int
    ): Boolean {
        return jump(sourceActivity, url, null, true, requestCode, flag)
    }

    fun jumpForResult(
        sourceActivity: Context?,
        url: String,
        extraParams: HashMap<String?, String?>?,
        requestCode: Int
    ): Boolean {
        return jump(sourceActivity, url, extraParams, true, requestCode, -1)
    }

    private fun jump(
        sourceActivity: Context?,
        url: String,
        extraParams: HashMap<String?, String?>?,
        forResult: Boolean,
        requestCode: Int,
        flag: Int
    ): Boolean {
        if (sourceActivity == null || TextUtils.isEmpty(url)) {
            return false
        }
        val jumpAction = JumpParser.parseAction(sourceActivity.javaClass.name, url)
        if (jumpAction != null && jumpAction.isValid && !isFastJump(jumpAction)) {
            jumpAction.addParams(extraParams)
            return excuteJump(sourceActivity, jumpAction, forResult, requestCode, flag)
        }
        return false
    }

    private fun excuteJump(
        sourceActivity: Context,
        jumpAction: JumpAction,
        forResult: Boolean,
        requestCode: Int,
        flag: Int
    ): Boolean {
        return if (forResult) {
            excuteNativeJumpActionForResult(
                sourceActivity,
                jumpAction,
                requestCode,
                flag
            )
        } else {
            excuteNativeJumpAction(sourceActivity, jumpAction, flag)
        }
    }

    private fun excuteNativeJumpAction(
        sourceContext: Context,
        jumpAction: JumpAction,
        flag: Int
    ): Boolean {
        val intent = Intent()
        if (!TextUtils.isEmpty(jumpAction.firstDestination)) {
            val destinationClass =
                getDestinationClass(jumpAction.firstDestination)
            if (destinationClass != null) {
                intent.setClass(sourceContext, destinationClass)
                jumpAction.putParamsToBundle(intent, jumpAction.getParamMap())
                if (flag > 0) {
                    intent.addFlags(flag)
                }
                sourceContext.startActivity(intent)
                return true
            }
        }
        return false
    }

    private fun excuteNativeJumpActionForResult(
        sourceContext: Context,
        jumpAction: JumpAction,
        requestCode: Int,
        flag: Int
    ): Boolean {
        try {
            val intent = Intent()
            if (!TextUtils.isEmpty(jumpAction.firstDestination)) {
                val destinationClass =
                    getDestinationClass(jumpAction.firstDestination)
                if (destinationClass != null) {
                    intent.setClass(sourceContext, destinationClass)
                    jumpAction.putParamsToBundle(intent, jumpAction.getParamMap())
                    if (flag > 0) {
                        intent.addFlags(flag)
                    }
                    (sourceContext as Activity).startActivityForResult(intent, requestCode)
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getIntentByUrl(
        url: String?,
        sourceContext: Context,
        flag: Int
    ): Intent? {
        val jumpAction =
            JumpParser.parseAction(sourceContext.javaClass.simpleName, url)
        var intent: Intent? = null
        if (jumpAction != null && !TextUtils.isEmpty(jumpAction.firstDestination)) {
            val destinationClass =
                getDestinationClass(jumpAction.firstDestination)
            if (destinationClass != null) {
                intent = Intent()
                intent.setClass(sourceContext, destinationClass)
                jumpAction.putParamsToBundle(intent, jumpAction.getParamMap())
                if (flag > 0) {
                    intent.addFlags(flag)
                }
            }
        }
        return intent
    }

    private fun getDestinationClass(destination: String?): Class<*>? {
        var destinationClass: Class<*>? = null
        if (!TextUtils.isEmpty(destination)) {
            val className = mJumpTable[destination]
            if (!TextUtils.isEmpty(className)) {
                destinationClass = try { Class.forName(className) } catch (e: Exception) { null }
            }
        }
        return destinationClass
    }

    private fun isFastJump(jumpAction: JumpAction?): Boolean {
        if (jumpAction == null) {
            return true
        }
        var flag = true
        val key = jumpAction.sourcePage + ":" + jumpAction.firstDestination
        val curClickTime = System.currentTimeMillis()
        if (Math.abs(curClickTime - getLastJumpTime(key)) >= MIN_JUMP_DELAY_TIME) {
            mJumpTimeMap[key] = curClickTime
            flag = false
        }
        return flag
    }

    private fun getLastJumpTime(key: String): Long {
        val time = mJumpTimeMap[key]
        return time ?: 0L
    }
}