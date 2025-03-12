package com.giang.applock20.screen.home.allapps

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentAllAppsBinding
import com.giang.applock20.util.AppInfoUtil.listAppInfo
import com.giang.applock20.util.AppInfoUtil.listLockedAppInfo


class AllAppsFragment : BaseFragment<FragmentAllAppsBinding>() {

    private lateinit var allAppsAdapter: AllAppsAdapter

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentAllAppsBinding {
        return FragmentAllAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            allAppsAdapter = AllAppsAdapter(listAppInfo) { clickedAppInfo ->
                // Avoid adding the same app to listLockedAppInfo multiple times when the user clicks repeatedly
                if(!listLockedAppInfo.contains(clickedAppInfo)) {
                    //Ensure that DiffUtil can accurately detect changes between the old and new lists
                    val tempList = listAppInfo.filterNot { it == clickedAppInfo }
                    listLockedAppInfo.add(clickedAppInfo)
                    allAppsAdapter.setNewList(tempList)
                    listAppInfo.remove(clickedAppInfo)
                }

            }

            recyclerView.adapter = allAppsAdapter
            recyclerView.itemAnimator = allAppsAdapter.SlideOutRightItemAnimator()


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
        val filteredList = listAppInfo.filter {
            it.name.lowercase().contains(text.lowercase())
        }

        allAppsAdapter.setNewList(filteredList)
        if (filteredList.isEmpty()) Toast.makeText(
            requireContext(),
            "No data found",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun handleEvent() {

    }
}
