package com.setruth.mvvmbaseproject.network.api

import com.setruth.mvvmbaseproject.network.response.TestBaseResponse
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST

interface TestAPI {
    /**
     * TODO 登录请求
     *
     * @param requestBody
     * @return
     */
    @Headers("Content-Type: application/json;charset=utf-8", "Accept: application/json")
    @POST("/user/login")
     fun login(): Call<TestBaseResponse<String>>
}