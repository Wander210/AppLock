package com.giang.applock20.screen.home.locked_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.base.BaseFragment
import com.giang.applock20.databinding.FragmentLockedAppsBinding
import com.giang.applock20.util.AppInfoUtil

class LockedAppFragment : BaseFragment<FragmentLockedAppsBinding>() {

    private lateinit var lockedAppAdapter: LockedAppAdapter
    private var checkBox: Boolean = false
    private var lastButtonClickTime = 0L
    
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
                        AppInfoUtil.listLockedAppInfo) {
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
                if(lockedAppAdapter.count != 0) {
                    AppInfoUtil.transferAppInfo(
                        requireContext(),
                        lockedAppAdapter.booleanArray,
                        AppInfoUtil.listAppInfo,
                        AppInfoUtil.listLockedAppInfo) {lockedAppAdapter.setNewList(it)}
                    lockedAppAdapter.booleanArray = BooleanArray(AppInfoUtil.listLockedAppInfo.size)
                    lockedAppAdapter.count = 0
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
                        lockedAppAdapter.updateAllPosition(true)
                        updateBtnLock()
                    } else {
                        cbSelectAll.setBackgroundResource(R.drawable.checkbox_unchecked)
                        tvSelectOrRemove.text = ContextCompat.getString(tvSelectOrRemove.context, R.string.select_all)
                        checkBox = false
                        lockedAppAdapter.updateAllPosition(false)
                        updateBtnLock()
                    }
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateBtnLock() {
        binding.apply {
            if(lockedAppAdapter.count != 0) {
                btnLock.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_active_button))
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                tvLock.text = "(${lockedAppAdapter.count}) Lock"
            }
            else {
                btnLock.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bg_inactive_button))
                tvLock.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_text))
                tvLock.text = "Lock"
            }
        }
    }
}
