package com.giang.applock20.screen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giang.applock20.R
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentLockedAppsBinding

class LockedAppsFragment : BaseFragment<FragmentLockedAppsBinding>() {
    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentLockedAppsBinding {
        return FragmentLockedAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {
    }

    override fun setupView() {
    }

    override fun handleEvent() {
    }


}