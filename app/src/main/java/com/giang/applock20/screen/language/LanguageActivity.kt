package com.giang.applock20.screen.language

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivityLanguageBinding
import com.giang.applock20.model.Language
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.constant.EXTRA_FROM_SPLASH
import com.giang.applock20.screen.home.HomeActivity
import com.giang.applock20.screen.set_new_lock_pattern.SetLockPatternActivity

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {

    private lateinit var languageAdapter: LanguageItemAdapter
    private val languageList by lazy { ArrayList<Language>() }
    private var curLanguage: String? = null
    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun initData() {
        curLanguage = MyPreferences.read(MyPreferences.PREF_LANGUAGE, "en")
        var temp: Language? = null
        languageList.addAll(
            listOf(
                Language(0, getString(R.string.english), R.drawable.ic_english, "en"),
                Language(1, getString(R.string.korean), R.drawable.ic_korean, "kr"),
                Language(2, getString(R.string.portuguese), R.drawable.ic_portugal, "pt"),
                Language(3, getString(R.string.spanish), R.drawable.ic_spanish, "es"),
                Language(4, getString(R.string.japanese), R.drawable.ic_japanese, "ja"),
                Language(5, getString(R.string.german), R.drawable.ic_german, "de"),
                Language(6, getString(R.string.polish), R.drawable.ic_polish, "pl"),
                Language(7, getString(R.string.italian), R.drawable.ic_italian, "it"),
                Language(8, getString(R.string.french), R.drawable.ic_french, "fr"),
                Language(9, getString(R.string.hindi), R.drawable.ic_hindi, "hi"),
                Language(10, getString(R.string.vietnamese), R.drawable.ic_vietnamese, "vi")
            )
        )

        for (i in languageList.indices) {
            if (languageList[i].locale == curLanguage) {
                temp = languageList[i]
                languageList.removeAt(i)
                break
            }
        }
        temp?.let {
            languageList.add(0, it)
        }
    }

    override fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@LanguageActivity)
        languageAdapter = LanguageItemAdapter(languageList) { selectedLanguage ->
            curLanguage = selectedLanguage.locale
            curLanguage?.let {
                languageAdapter.updateSelectedPosition(it)
            }
        }
        //...The case when the user has not selected any item yet.
        curLanguage?.let {
            languageAdapter.updateSelectedPosition(it)
        }
        binding.recyclerView.adapter = languageAdapter
    }

    override fun handleEvent() {
        binding.btnDone.setOnClickListener {
            curLanguage?.let {
                MyPreferences.write(MyPreferences.PREF_LANGUAGE, it)
                if (intent.getBooleanExtra(EXTRA_FROM_SPLASH, false)) {
                    startActivity(Intent(this@LanguageActivity, SetLockPatternActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@LanguageActivity, HomeActivity::class.java).apply {
                        // clear activity cu trong stack
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                    finish()
                }
            }
        }
    }
}
