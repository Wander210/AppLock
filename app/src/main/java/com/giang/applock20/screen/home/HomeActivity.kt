package com.giang.applock20.screen.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.giang.applock20.screen.base.BaseActivity
import com.giang.applock20.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        setupView()
        handleEvent()
    }

    private fun initData() {
    }

    private fun setupView() {
    }

    private fun handleEvent() {
    }
}