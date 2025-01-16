package com.giang.applock20.util
import android.content.Context
import com.giang.applock20.preference.MyPreferences
import java.util.Locale

object LanguageUtil {
    fun setLanguage(context: Context?) {
        if (context == null) return
        var language: String? = MyPreferences.read(MyPreferences.PREF_LANGUAGE, null)
        if (language == null) {
            language = Locale.getDefault().language
        }
        val newLocale = Locale(language!!.lowercase(Locale.getDefault()))
        Locale.setDefault(newLocale)
        val res = context.resources
        val conf = res.configuration
        conf.setLocale(newLocale)
        res.updateConfiguration(conf, res.displayMetrics)
    }
}
