package com.setruth.mvvmbaseproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.setruth.mvvmbaseproject.viewmodel.PublicViewModel

/**
 * @Author: Setruth
 * @Date: 2023/3/21 10:11
 */

/**
 * TODO 基类Fragment
 * @param VB ViewBinding类型
 * @param VM fragment自己对应的ViewModel类型(可选项)
 * @property inflate 视图类的绑定方法
 * @property viewModelClass ViewModel类型可以为空，此时的viewModel获取会错误
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val viewModelClass: Class<VM>?,
    publicViewModelTag: Boolean = false
) : Fragment() {
    private var bufferRootView: View? = null
    private var binding: VB? = null
    private val viewModel: VM? by lazy {
        val viewModelProvider = ViewModelProvider(this)
        viewModelClass?.let {
            viewModelProvider[viewModelClass]
        }
    }
    val publicViewModel: PublicViewModel? by lazy {
        if (publicViewModelTag) {
            ViewModelProvider(requireActivity())[PublicViewModel::class.java]
        } else {
            null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bufferRootView?.let {
            return bufferRootView
        }
        binding = inflate(inflater, container, false)
        initFragment(binding!!, viewModel, savedInstanceState)
        bufferRootView = binding!!.root
        return binding!!.root
    }

    abstract fun initFragment(binding: VB, viewModel: VM?, savedInstanceState: Bundle?)
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
