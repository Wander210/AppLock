package com.giang.applock20.screen.home.locked_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.dao.AppInfoDatabase
import com.giang.applock20.databinding.FragmentLockedAppsBinding
import com.giang.applock20.util.AppInfoUtil
import com.giang.applock20.util.AppInfoUtil.listLockedAppInfo
import com.giang.applock20.util.AppInfoUtil.listAppInfo
import com.giang.applock20.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LockedAppFragment : BaseFragment<FragmentLockedAppsBinding>() {

    private lateinit var lockedAppAdapter: LockedAppAdapter
    private var checkBox: Boolean = false

    override fun onResume() {
        super.onResume()
        val tempList = listLockedAppInfo
        lockedAppAdapter.setNewList(tempList)
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): FragmentLockedAppsBinding {
        return FragmentLockedAppsBinding.inflate(layoutInflater)
    }

    override fun initData() {}

    override fun setupView() {
        updateComponentVisibility()
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            lockedAppAdapter = LockedAppAdapter(requireContext(), listLockedAppInfo) { clickedAppInfo, position ->
                lockedAppAdapter.updateSelectedPosition(clickedAppInfo)
                updateBtnLock()
            }
            recyclerView.adapter = lockedAppAdapter
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
                        listLockedAppInfo) {
                        lockedAppAdapter.setNewList(it)
                    }
                    return true
                }
            })
        }
    }

    override fun handleEvent() {
        binding.apply {
            btnLock.setOnClickListener({
                if (lockedAppAdapter.mapSelectedApp.isNotEmpty()) {
                    val transferList: MutableList<AppInfo> = mutableListOf()

                    // Update the database
                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppInfoDatabase.getInstance(requireContext())
                        lockedAppAdapter.mapSelectedApp.forEach { (key, value) ->
                            db.appInfoDAO().updateAppLockStatus(key, false)
                        }

                        // Update the unlocked app list
                        listLockedAppInfo.forEach {
                            if (lockedAppAdapter.mapSelectedApp[it.packageName] != null) {
                                transferList.add(it)
                            }
                        }
                        AppInfoUtil.insertSortedAppInfo(listAppInfo, transferList)

                        // Update the locked app list and UI
                        withContext(Dispatchers.Main) {
                            lockedAppAdapter.mapSelectedApp.clear()
                            val tempList = listLockedAppInfo.filterNot { it in transferList }
                            lockedAppAdapter.setNewList(tempList)
                            listLockedAppInfo.clear()
                            listLockedAppInfo.addAll(tempList)

                            // Update the btnLock
                            updateBtnLock()
                            updateComponentVisibility()
                        }
                    }
                }
            })

            cbSelectAll.setOnClickListener({
                if (!checkBox) {
                    cbSelectAll.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.checkbox_checked
                        )
                    )
                    tvSelectOrRemove.text = ContextCompat.getString(tvSelectOrRemove.context, R.string.remove_all)
                    checkBox = true
                    lockedAppAdapter.updateAllPosition(true)
                    updateBtnLock()
                } else {
                    cbSelectAll.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.checkbox_unchecked
                        )
                    )
                    tvSelectOrRemove.text = ContextCompat.getString(tvSelectOrRemove.context, R.string.select_all)
                    checkBox = false
                    lockedAppAdapter.updateAllPosition(false)
                    updateBtnLock()
                }
            })
        }
    }

    fun updateComponentVisibility() {
        binding.apply {
            if(listLockedAppInfo.isEmpty()) {
                btnLock.visibility = INVISIBLE
                tvLock.visibility = INVISIBLE
                cbSelectAll.visibility = INVISIBLE
                tvSelectOrRemove.visibility = INVISIBLE
            } else {
                btnLock.visibility = VISIBLE
                tvLock.visibility = VISIBLE
                cbSelectAll.visibility = VISIBLE
                tvSelectOrRemove.visibility = VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateBtnLock() {
        binding.apply {
            if (lockedAppAdapter.mapSelectedApp.isNotEmpty()) {
                btnLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_active_button
                    )
                )
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                tvLock.text = "(${lockedAppAdapter.mapSelectedApp.size}) Unlock"
            } else {
                btnLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_inactive_button
                    )
                )
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_text))
                tvLock.text = "Unlock"
            }
        }
    }
}