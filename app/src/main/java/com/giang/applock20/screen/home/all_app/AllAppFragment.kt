package com.giang.applock20.screen.home.all_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.dao.AppInfoDatabase
import com.giang.applock20.databinding.FragmentAllAppsBinding
import com.giang.applock20.model.AppInfo
import com.giang.applock20.util.AppInfoUtil
import com.giang.applock20.util.AppInfoUtil.listAppInfo
import com.giang.applock20.util.AppInfoUtil.listLockedAppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllAppFragment : BaseFragment<FragmentAllAppsBinding>() {

    private lateinit var allAppAdapter: AllAppAdapter
    private var checkBox: Boolean = false
    private var lastButtonClickTime = 0L

    override fun onResume() {
        super.onResume()
        val tempList = listAppInfo
        allAppAdapter.setNewList(tempList)
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentAllAppsBinding {
        return FragmentAllAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {}

    override fun setupView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            allAppAdapter = AllAppAdapter(listAppInfo) { clickedAppInfo ->
                allAppAdapter.updateSelectedPosition(clickedAppInfo)
                updateBtnLock()

            }
            recyclerView.adapter = allAppAdapter
            recyclerView.itemAnimator = SlideOutRightItemAnimator()

            searchBar.clearFocus()
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    AppInfoUtil.filterList(
                        requireContext(),
                        newText ?: "",
                        listAppInfo) {
                        allAppAdapter.setNewList(it)
                    }
                    return true
                }
            })
        }

    }

    override fun handleEvent() {
        binding.apply {
            btnLock.setOnClickListener({
                if(allAppAdapter.count != 0) {
                    val transferList: MutableList<AppInfo> = mutableListOf()

                    //Update the database
                    //Guarantee that the lists do not change while iterating
                    val flagsSnapshot = allAppAdapter.booleanArray.copyOf()
                    val pkgSnapshot   = listAppInfo.toList()

                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppInfoDatabase.getInstance(requireContext())
                        for(i in flagsSnapshot.indices) {
                            if(flagsSnapshot[i])
                                db.appInfoDAO().updateAppLockStatus(pkgSnapshot[i].packageName, true)
                        }
                    }

                    //Update the locked app list
                    for(i in allAppAdapter.booleanArray.indices) {
                        if(allAppAdapter.booleanArray[i]) transferList.add(listAppInfo[i])
                    }
                    AppInfoUtil.insertSortedAppInfo(listLockedAppInfo, transferList)

                    //Update the app list and ui
                    //Ensure that DiffUtil can accurately detect changes between the old and new lists
                    val tempList = listAppInfo.filterNot {it in transferList}
                    allAppAdapter.setNewList(tempList)
                    listAppInfo.clear()
                    listAppInfo.addAll(tempList)

                    //Update the btnLock
                    allAppAdapter.count = 0
                    updateBtnLock()
                }
            })

            cbSelectAll.setOnClickListener({
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastButtonClickTime > 1000) {
                    lastButtonClickTime = currentTime
                    if(!checkBox) {
                        cbSelectAll.setBackgroundResource(R.drawable.checkbox_checked)
                        tvSelectOrRemove.text = ContextCompat.getString(tvSelectOrRemove.context, R.string.remove_all)
                        checkBox = true
                        allAppAdapter.updateAllPosition(true)
                        updateBtnLock()
                    } else {
                        cbSelectAll.setBackgroundResource(R.drawable.checkbox_unchecked)
                        tvSelectOrRemove.text = ContextCompat.getString(tvSelectOrRemove.context, R.string.select_all)
                        checkBox = false
                        allAppAdapter.updateAllPosition(false)
                        updateBtnLock()
                    }
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateBtnLock() {
        binding.apply {
            if(allAppAdapter.count != 0) {
                btnLock.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_active_button))
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                tvLock.text = "(${allAppAdapter.count}) Lock"
            }
            else {
                btnLock.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_inactive_button))
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_text))
                tvLock.text = "Lock"
            }
        }
    }
}
