package com.setruth.mvvmbaseproject.ui.mainnavigation

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.setruth.mvvmbaseproject.R
import com.setruth.mvvmbaseproject.base.BaseFragment
import com.setruth.mvvmbaseproject.databinding.FragmentLoginLayoutBinding
import com.setruth.mvvmbaseproject.databinding.FragmentMainNavigationBinding
import com.setruth.mvvmbaseproject.ui.login.LoginFragmentViewModel

/**
 * TODO 主要的导航页面
 * @Author: Setruth
 * @Date: 2023/3/26 15:49
 */
class MainNavigationFragment : BaseFragment<FragmentMainNavigationBinding, LoginFragmentViewModel>(
    FragmentMainNavigationBinding::inflate,
    null
) {
    override fun initFragment(
        binding: FragmentMainNavigationBinding,
        viewModel: LoginFragmentViewModel?,
        savedInstanceState: Bundle?
    ) {
        (childFragmentManager.findFragmentById(R.id.main_view_nav) as NavHostFragment).apply {
            binding.bottomNav.setupWithNavController(this.navController)
        }
    }

}