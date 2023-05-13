package com.setruth.mvvmbaseproject.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.setruth.mvvmbaseproject.R
import com.setruth.mvvmbaseproject.base.BaseFragment
import com.setruth.mvvmbaseproject.databinding.FragmentLoginLayoutBinding
import com.setruth.mvvmbaseproject.network.ApiResponse
import com.setruth.mvvmbaseproject.network.api.TestAPI
import com.setruth.mvvmbaseproject.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

/**
 * @Author: Setruth
 * @Date: 2023/3/26 11:04
 */
class LoginFragment : BaseFragment<FragmentLoginLayoutBinding, LoginFragmentViewModel>(
    FragmentLoginLayoutBinding::inflate,
    LoginFragmentViewModel::class.java,
    true
) {
    override fun initFragment(
        binding: FragmentLoginLayoutBinding,
        viewModel: LoginFragmentViewModel?,
        savedInstanceState: Bundle?
    ) {
        viewModel!!.apply {
            binding.viewModel = this
            binding.loginBtn.setOnClickListener {

                if (loginAccount.value == "" || loginPassword.value == "") {
                    Toast.makeText(requireContext(), "账户或密码不能为空", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loginRequest(loginAccount.value!!, loginPassword.value!!)
                }
            }
        }
    }

    private fun loginRequest(act: String, pwd: String) {
        publicViewModel?.apply {
            request(TestAPI::class.java).login().getResponse {
                it.collect{
                    when (it) {
                        is ApiResponse.Error -> LogUtil.e("${it.errMsg} ${it.errMsg}")
                        ApiResponse.Loading -> LogUtil.e("Loading")
                        is ApiResponse.Success -> {
                            LogUtil.e("${it.data.toString()}")
                            withContext(Dispatchers.Main){
                                findNavController().navigate(
                                    R.id.mainNavigationFragment,
                                    null,
                                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}