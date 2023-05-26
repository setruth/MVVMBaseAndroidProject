package com.setruth.mvvmbaseproject.ui.dbtest

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.setruth.mvvmbaseproject.base.BaseFragment
import com.setruth.mvvmbaseproject.databinding.FragmentDbBinding
import com.setruth.mvvmbaseproject.repository.MyDataBase
import com.setruth.mvvmbaseproject.repository.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentDBTest : BaseFragment<FragmentDbBinding, ViewModel>(
    FragmentDbBinding::inflate,
    null,
) {
    override fun initFragment(
        binding: FragmentDbBinding,
        viewModel: ViewModel?,
        savedInstanceState: Bundle?
    ) {
        val userDao = MyDataBase.getDB(requireContext()).getUserDao()
        val TAG = "db"
        binding.add.setOnClickListener {
            val account: String
            val nickname: String
            binding.apply {
                account = addAccount.text.toString()
                nickname = addNickname.text.toString()
            }
            CoroutineScope(Dispatchers.IO).launch {
                userDao.insertUser(UserEntity(act = account, nickname = nickname))
            }
        }
        binding.getAll.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                userDao.getAll().forEach {
                    Log.e(TAG, "all$it ")
                }
            }
        }
        binding.searchAccount.setOnClickListener {
            val inputSearch = binding.qAccountInput.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                userDao.getInfoByAct(inputSearch).forEach {
                    Log.e(TAG, "all$it ")
                }
            }
        }
    }
}