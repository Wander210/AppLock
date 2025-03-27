//package com.giang.applock20.util
//
//import android.content.Context
//import com.giang.applock20.R
//import com.giang.applock20.preference.MyPreferences
//
//object ThemeUtil {
//    fun getTheme(context: Context): Int {
//        val curTheme: Int = MyPreferences.read(MyPreferences.PREF_THEME, R.style.Theme_LightTheme)
//        when (curTheme) {
//            DARK_THEME -> return R.style.Theme_DarkTheme
//            LIGHT_THEME -> return R.style.Theme_LightTheme
//        }
//        return R.style.Theme_LightTheme
//    }
//
//    fun getResColor(context: Context, attr: Int): Int {
//        var intColor = 0
//        try {
//            val themeId = getTheme(context)
//            val a = context.theme.obtainStyledAttributes(themeId, intArrayOf(attr))
//            intColor = a.getColor(0, 0)
//            a.recycle()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return intColor
//    }
//
//    fun getHexColor(context: Context, attr: Int): String {
//        var hexColor = "#FFFFFF"
//        try {
//            val themeId = getTheme(context)
//            val a = context.theme.obtainStyledAttributes(themeId, intArrayOf(attr))
//            hexColor = Integer.toHexString(a.getColor(0, 0))
//            a.recycle()
//            return hexColor
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return hexColor
//    }
//}