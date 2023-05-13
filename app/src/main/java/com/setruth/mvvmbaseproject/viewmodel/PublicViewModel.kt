package com.setruth.mvvmbaseproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.setruth.doublewillow.utils.SPUtil
import com.setruth.mvvmbaseproject.network.ApiResponse
import com.setruth.mvvmbaseproject.network.RequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/**
 * TODO 公共的ViewModel
 * 共享到所有的Fragment中，在BaseFragment中就集成了这个PublicViewModel，可以在Activity加载时把这个公共的ViewModel进行初始化，
 * 类似一个单例的使用，可以初始化一次一些需要Context的对象，不用反复获取Context减小开销，并且不是单例进行Context的持有，避免不规范操作，
 * 比如初始化一次API类和一些数据库操作工具类，在别的Fragment中就可以反复的进行使用，从而减少不必要对象的反复创建，
 *
 * 不建议创建多个Public对象使得一个Fragment中进行多个ViewModel的操作，反而会让代码结构变得冗余和复杂，
 * 尽量两个ViewModel，一个fragment本身的ViewModel，一个公共的PublicViewModel
 * @Author: Setruth
 * @Date: 2023/3/26 15:28
 */
class PublicViewModel(application: Application) : AndroidViewModel(application) {

    val requestBuilder = RequestBuilder(application.applicationContext)
    val spUtil = SPUtil(application.applicationContext)
    fun <T> request(APIType: Class<T>): T = requestBuilder.getAPI(APIType)
    fun <T> Call<T>.getResponse(process: suspend (flow: Flow<ApiResponse<T>>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            requestBuilder.apply {
                process(requestBuilder.getResponse {
                    this@getResponse.execute()
                })
            }
        }
    }

}