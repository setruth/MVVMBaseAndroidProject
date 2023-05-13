package com.setruth.mvvmbaseproject.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author: Setruth
 * @Date: 2023/3/26 14:05
 */
class LoginFragmentViewModel : ViewModel() {
    val loginAccount by lazy {
        MutableLiveData("")
    }

    val loginPassword by lazy {
        MutableLiveData("")
    }
    val autoLoginState by lazy {
        MutableLiveData(false)
    }
    val rememberPasswordState by lazy {
        MutableLiveData(false)
    }
}