package com.giang.applock20.app

import android.app.Application
import com.giang.applock20.preference.MyPreferences

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MyPreferences.init(this)
    }
}