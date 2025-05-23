package com.giang.applock20.app

import android.app.Application
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.util.PermissionUtil
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        MyPreferences.init(this)
        PermissionUtil.init(this)
    }
}