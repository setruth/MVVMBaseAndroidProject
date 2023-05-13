package com.setruth.mvvmbaseproject.ui.mainnavigation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.setruth.mvvmbaseproject.R
import com.setruth.mvvmbaseproject.base.BaseFragment
import com.setruth.mvvmbaseproject.databinding.FragmentHomeBinding


/**
 * TODO
 * @Author: Setruth
 * @Date: 2023/3/27 19:53
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, ViewModel>(
    FragmentHomeBinding::inflate,
    null,
    true
) {
    @SuppressLint("ResourceType", "UseRequireInsteadOfGet")
    override fun initFragment(
        binding: FragmentHomeBinding,
        viewModel: ViewModel?,
        savedInstanceState: Bundle?
    ) {
        binding.fg2Btn.setOnClickListener {
            requireActivity().findNavController(R.id.app_nav).navigate(R.id.action_mainNavigationFragment_to_fragment2)
        }

    }
}