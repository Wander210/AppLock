package com.giang.applock20.service

object SystemPackages {
    fun getSystemPackages(): List<String> {
        return arrayListOf<String>().apply {
            add("com.android.packageinstaller")

        }
    }

    fun getOtherPackages(): List<String> {
        return arrayListOf<String>().apply {
            add("com.facebook.katana")
        }
    }

    fun getUnLockPackages(): List<String> {
        return arrayListOf<String>().apply {
            // Google Play Services – blocking this would break many background services
            add("com.google.android.gms")
            // Samsung’s default launcher – must stay unlocked so the user can access Home
            add("com.sec.android.app.launcher")
            // Xiaomi’s global launcher – keep unlocked for Xiaomi devices’ navigation
            add("com.mi.android.globallauncher")
            // Generic “launcher” keyword – catches other launchers to avoid locking the Home screen
            add("launcher")
        }
    }
}