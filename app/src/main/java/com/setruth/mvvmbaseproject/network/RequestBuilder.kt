package com.setruth.mvvmbaseproject.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RequestBuilder(context: Context) {
    private var retrofitBuilder: Retrofit

    init {
        OkHttpClient.Builder()
            .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)))
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
            .apply {
                retrofitBuilder = Retrofit.Builder()
                    .baseUrl(RequestConfiguration.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(this)
                    .build()
            }
    }

    /**
     * TODO 获取APi接口的实体请求类
     *
     * @param T
     * @param APIType
     * @return
     */
    fun <T> getAPI(APIType: Class<T>): T = retrofitBuilder.create(APIType)

    /**
     * TODO 获取响应
     *
     * @param T 响应类型
     * @param requestFun 请求函数
     * @return 返回flow根据emit的密封类的类型来判断网络请求状态
     */
    suspend fun <T> getResponse(requestFun: () -> Response<T>): Flow<ApiResponse<T>> =
        flow {
            emit(ApiResponse.Loading)
            try {
                with(requestFun()) {
                    if (isSuccessful) {
                        if (body() != null) ApiResponse.Success(body()!!) else ApiResponse.Error(
                            Exception("失败"),
                            "服务器异常"
                        )
                    } else {
                        ApiResponse.Error(Exception("${code()}"), "")
                    }.let {
                        emit(it)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e, "网络请求失败"))
            }
        }.catch { e ->
            emit(ApiResponse.Error(e as Exception, "网络请求失败"))
        }.flowOn(Dispatchers.IO)
}

/**
 *  TODO 网络请求配置
 */
object RequestConfiguration {
    const val URL = "http://10.0.2.2:1024"
}

/**
 * TODO 响应类
 *
 * @param T
 */
sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T?) : ApiResponse<T>()
    data class Error(val exception: Exception, val errMsg: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}


