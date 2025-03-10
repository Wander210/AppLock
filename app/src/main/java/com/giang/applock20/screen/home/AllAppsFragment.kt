package com.giang.applock20.screen.home

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentAllAppsBinding
import com.giang.applock20.screen.home.AppAdapter.SlideOutRightItemAnimator
import com.giang.applock20.util.AppInfoUtil

class AllAppsFragment : BaseFragment<FragmentAllAppsBinding>() {

    private lateinit var appAdapter: AppAdapter

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentAllAppsBinding {
        return FragmentAllAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            appAdapter = AppAdapter(AppInfoUtil.listAppInfo)
            recyclerView.adapter = appAdapter
            recyclerView.itemAnimator = appAdapter.SlideOutRightItemAnimator()


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
        val filteredList = AppInfoUtil.listAppInfo.filter {
            it.name.lowercase().contains(text.lowercase())
        }

        appAdapter.setFilteredList(filteredList)
        if (filteredList.isEmpty()) Toast.makeText(
            requireContext(),
            "No data found",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun handleEvent() {
    }
}
