package com.setruth.mvvmbaseproject.ui.mainnavigation.user

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.setruth.mvvmbaseproject.R
import com.setruth.mvvmbaseproject.base.BaseFragment
import com.setruth.mvvmbaseproject.databinding.FragmentUserBinding

/**
 * TODO
 * @Author: Setruth
 * @Date: 2023/3/27 19:53
 */

class UserFragment: BaseFragment<FragmentUserBinding, ViewModel>(
    FragmentUserBinding::inflate,
    null,
    true
) {
    override fun initFragment(
        binding: FragmentUserBinding,
        viewModel: ViewModel?,
        savedInstanceState: Bundle?
    ) {
        binding.logout.setOnClickListener {
            requireActivity().findNavController(R.id.app_nav).navigate(
                R.id.loginFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.mainNavigationFragment, true)
                    .build()
            )
        }
    }
}