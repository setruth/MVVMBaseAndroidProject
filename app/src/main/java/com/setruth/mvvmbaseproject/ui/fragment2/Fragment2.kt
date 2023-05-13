package com.setruth.mvvmbaseproject.ui.fragment2

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.setruth.mvvmbaseproject.base.BaseFragment
import com.setruth.mvvmbaseproject.databinding.FragmentTest2Binding

/**
 * TODO
 * @Author: Setruth
 * @Date: 2023/5/3 21:56
 */
class Fragment2 : BaseFragment<FragmentTest2Binding, ViewModel>(
    FragmentTest2Binding::inflate,
    null,
    true
) {
    override fun initFragment(
        binding: FragmentTest2Binding,
        viewModel: ViewModel?,
        savedInstanceState: Bundle?
    ) {

    }
}