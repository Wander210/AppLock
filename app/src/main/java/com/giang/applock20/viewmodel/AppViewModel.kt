package com.giang.applock20.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giang.applock20.model.AppInfo

class AppViewModel : ViewModel() {
    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList: LiveData<List<AppInfo>> get() = _appList

    fun setAppList(list: List<AppInfo>) {
        _appList.value = list
    }
}
