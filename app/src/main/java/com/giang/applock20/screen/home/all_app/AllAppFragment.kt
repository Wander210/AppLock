package com.giang.applock20.screen.home.all_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
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
import kotlinx.coroutines.withContext

class AllAppFragment : BaseFragment<FragmentAllAppsBinding>() {

    private lateinit var allAppAdapter: AllAppAdapter
    private var checkBox: Boolean = false

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
        updateComponentVisibility()
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            allAppAdapter =
                AllAppAdapter(requireContext(), listAppInfo) { clickedAppInfo, position ->
                    allAppAdapter.updateSelectedPosition(clickedAppInfo, position)
                    updateBtnLock()
                }
            recyclerView.adapter = allAppAdapter
            binding.recyclerView.itemAnimator = SlideOutRightItemAnimator()

            searchBar.clearFocus()
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    AppInfoUtil.filterList(
                        requireContext(),
                        newText ?: "",
                        listAppInfo
                    ) {
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
                if (allAppAdapter.mapSelectedApp.isNotEmpty()) {
                    val transferList: MutableList<AppInfo> = mutableListOf()

                    //Update the database
                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppInfoDatabase.getInstance(requireContext())
                        allAppAdapter.mapSelectedApp.forEach { (key, value) ->
                            db.appInfoDAO().updateAppLockStatus(key, true)
                        }
                        //Update the locked app list
                        listAppInfo.forEach {
                            if (allAppAdapter.mapSelectedApp[it.packageName] != null) {
                                transferList.add(it)
                            }
                        }
                        AppInfoUtil.insertSortedAppInfo(listLockedAppInfo, transferList)

                        //Update the app list and ui
                        //Ensure that DiffUtil can accurately detect changes between the old and new lists
                        withContext(Dispatchers.Main) {
                            allAppAdapter.mapSelectedApp.clear()
                            val tempList = listAppInfo.filterNot {it in transferList}
                            allAppAdapter.setNewList(tempList)
                            listAppInfo.clear()
                            listAppInfo.addAll(tempList)
                            //Update the btnLock
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
                    tvSelectOrRemove.text =
                        ContextCompat.getString(tvSelectOrRemove.context, R.string.remove_all)
                    checkBox = true
                    allAppAdapter.updateAllPosition(true)
                    updateBtnLock()
                } else {
                    cbSelectAll.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.checkbox_unchecked
                        )
                    )
                    tvSelectOrRemove.text =
                        ContextCompat.getString(tvSelectOrRemove.context, R.string.select_all)
                    checkBox = false
                    allAppAdapter.updateAllPosition(false)
                    updateBtnLock()
                }
            })
        }
    }

    fun updateComponentVisibility() {
        binding.apply {
            if(listAppInfo.isEmpty()) {
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
            if (allAppAdapter.mapSelectedApp.isNotEmpty()) {
                btnLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_active_button
                    )
                )
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                tvLock.text = "(${allAppAdapter.mapSelectedApp.size}) Lock"
            } else {
                btnLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_inactive_button
                    )
                )
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_text))
                tvLock.text = "Lock"
            }
        }
    }
}
