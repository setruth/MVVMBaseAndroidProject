package com.setruth.mvvmbaseproject.ui.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.setruth.mvvmbaseproject.databinding.ActivityMainBinding
import com.setruth.mvvmbaseproject.viewmodel.PublicViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        ViewModelProvider(this)[PublicViewModel::class.java]
        setContentView(binding.root)
    }
}
