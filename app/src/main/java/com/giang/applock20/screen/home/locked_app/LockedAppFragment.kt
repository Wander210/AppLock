package com.giang.applock20.screen.home.locked_app

import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentLockedAppsBinding
import com.giang.applock20.util.AppInfoUtil

class LockedAppFragment : BaseFragment<FragmentLockedAppsBinding>() {

    private lateinit var lockedAppAdapter: LockedAppAdapter

    override fun onResume() {
        super.onResume()
        lockedAppAdapter.setNewList(AppInfoUtil.listLockedAppInfo)

    }

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentLockedAppsBinding {
        return FragmentLockedAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {}

    override fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            lockedAppAdapter = LockedAppAdapter(AppInfoUtil.listLockedAppInfo) { clickedAppInfo ->
                AppInfoUtil.transferAppInfo(
                    requireContext(),
                    clickedAppInfo,
                    AppInfoUtil.listAppInfo,
                    AppInfoUtil.listLockedAppInfo) {
                    lockedAppAdapter.setNewList(it)
                }
            }
            recyclerView.adapter = lockedAppAdapter
            recyclerView.itemAnimator = lockedAppAdapter.SlideOutRightItemAnimator()


            searchBar.clearFocus()
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    AppInfoUtil.filterList(
                        requireContext(),
                        newText ?: "",
                        AppInfoUtil.listLockedAppInfo) {
                        lockedAppAdapter.setNewList(it)
                    }
                    return true
                }
            })

        }

    }

    override fun handleEvent() {}
}