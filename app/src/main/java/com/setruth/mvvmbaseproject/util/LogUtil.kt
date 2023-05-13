package com.setruth.mvvmbaseproject.util

import android.util.Log

object LogUtil {
    private const val TAG="LogUtil"
    private const val DEBUG=true
    fun d(msg:String)=DEBUG?.let { Log.d(TAG,msg )}
    fun e(msg:String)=DEBUG?.let { Log.e(TAG,msg )}
    fun i(msg:String)=DEBUG?.let { Log.i(TAG,msg )}
    fun w(msg:String)=DEBUG?.let { Log.w(TAG,msg )}
}