package com.giang.applock20.screen.home.all_app

import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentAllAppsBinding
import com.giang.applock20.util.AppInfoUtil


class AllAppFragment : BaseFragment<FragmentAllAppsBinding>() {

    private lateinit var allAppAdapter: AllAppAdapter

    override fun onResume() {
        super.onResume()
        allAppAdapter.setNewList(AppInfoUtil.listAppInfo)
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentAllAppsBinding {
        return FragmentAllAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {}

    override fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            allAppAdapter = AllAppAdapter(AppInfoUtil.listAppInfo) { clickedAppInfo ->
                AppInfoUtil.transferAppInfo(
                    requireContext(),
                    clickedAppInfo,
                    AppInfoUtil.listLockedAppInfo,
                    AppInfoUtil.listAppInfo) {
                    allAppAdapter.setNewList(it)
                }

            }

            recyclerView.adapter = allAppAdapter
            recyclerView.itemAnimator = allAppAdapter.SlideOutRightItemAnimator()


            searchBar.clearFocus()
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    AppInfoUtil.filterList(
                        requireContext(),
                        newText ?: "",
                        AppInfoUtil.listAppInfo) {
                        allAppAdapter.setNewList(it)
                    }
                    return true
                }
            })
        }

    }

    override fun handleEvent() {}
}
