package com.giang.applock20.screen.home

import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentAllAppsBinding
import com.giang.applock20.model.AppInfo
import com.giang.applock20.viewmodel.AppViewModel
import com.giang.applock20.util.LoadAppsUtil  // Ensure you import this
import kotlinx.coroutines.launch

class AllAppsFragment : BaseFragment<FragmentAllAppsBinding>() {

    private val viewModel: AppViewModel by viewModels()
    private lateinit var appAdapter: AppAdapter

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentAllAppsBinding {
        return FragmentAllAppsBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val currentList = viewModel.appList.value ?: emptyList()
        appAdapter = AppAdapter(currentList)
        binding.recyclerView.adapter = appAdapter
    }

    override fun setupView() {
    }

    override fun handleEvent() {
    }
}
