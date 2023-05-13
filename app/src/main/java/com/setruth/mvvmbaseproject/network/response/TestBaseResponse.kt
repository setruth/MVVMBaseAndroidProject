package com.setruth.mvvmbaseproject.network.response

data class TestBaseResponse<T>(
    val status: String ,

    val message: String,

    var data: T?
)
