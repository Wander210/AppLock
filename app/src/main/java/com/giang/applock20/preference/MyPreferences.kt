package com.giang.applock20.preference

import android.content.Context
import android.content.SharedPreferences

object MyPreferences {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "shared_preferences"
    const val PREF_LANGUAGE = "pref_language"
    const val PREF_LOCK_PATTERN = "pref_lock_pattern"
    const val PREF_IS_PREVENT_UNINSTALL = "pref_is_prevent_uninstall"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: String?): String? {
        return prefs.getString(key, value)
    }

    fun write(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }

    fun read(key: String, value: Boolean): Boolean {
        return prefs.getBoolean(key, value)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean(key, value)
            commit()
        }
    }
}

