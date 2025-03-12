package com.giang.applock20.screen.home.lockedapps

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentLockedAppsBinding
import com.giang.applock20.util.AppInfoUtil.listAppInfo
import com.giang.applock20.util.AppInfoUtil.listLockedAppInfo


class LockedAppsFragment : BaseFragment<FragmentLockedAppsBinding>() {

    private lateinit var lockedAppsAdapter: LockedAppsAdapter

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentLockedAppsBinding {
        return FragmentLockedAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            lockedAppsAdapter = LockedAppsAdapter(listLockedAppInfo) { clickedAppInfo ->
                if(!listAppInfo.contains(clickedAppInfo)) {
                    val tempList = listLockedAppInfo.filterNot { it == clickedAppInfo }
                    listAppInfo.add(clickedAppInfo)
                    lockedAppsAdapter.setNewList(tempList)
                    listLockedAppInfo.remove(clickedAppInfo)
                }
            }
            recyclerView.adapter = lockedAppsAdapter
            recyclerView.itemAnimator = lockedAppsAdapter.SlideOutRightItemAnimator()


            searchBar.clearFocus()
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText ?: "")
                    return true
                }
            })

        }

    }

    private fun filterList(text : String) {
        val filteredList = listLockedAppInfo.filter {
            it.name.lowercase().contains(text.lowercase())
        }

        lockedAppsAdapter.setNewList(filteredList)
        if (filteredList.isEmpty()) Toast.makeText(
            requireContext(),
            "No data found",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun handleEvent() {

    }
}