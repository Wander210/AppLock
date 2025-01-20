	package com.giang.applock20.screen.language

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivityLanguageBinding
import com.giang.applock20.model.Language
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.base.BaseActivity
import com.giang.applock20.screen.home.HomeActivity
import com.giang.applock20.util.LanguageUtil

    class LanguageActivity : BaseActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var languageAdapter: LanguageItemAdapter
    private val languageList by lazy { ArrayList<Language>() }
    private var curLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        setupView()
        handleEvent()
    }

    private fun initData() {
        curLanguage = MyPreferences.read(MyPreferences.PREF_LANGUAGE, "en")
        var temp : Language? = null
        languageList.addAll(listOf(
            Language(0, getString(R.string.english), R.drawable.english_ic, "en"),
            Language(1, getString(R.string.korean), R.drawable.korean_ic, "kr"),
            Language(2, getString(R.string.portuguese), R.drawable.portugal_ic, "pt"),
            Language(3, getString(R.string.spanish), R.drawable.spanish_ic, "es"),
            Language(4, getString(R.string.japanese), R.drawable.japanese_ic, "ja"),
            Language(5, getString(R.string.german), R.drawable.german_ic, "de"),
            Language(6, getString(R.string.polish), R.drawable.polish_ic, "pl"),
            Language(7, getString(R.string.italian), R.drawable.italian_ic, "it"),
            Language(8, getString(R.string.french), R.drawable.french_ic, "fr"),
            Language(9, getString(R.string.hindi), R.drawable.hindi_ic, "hi"),
            Language(10, getString(R.string.vietnamese), R.drawable.vietnamese_ic, "vi")
        ))

        for(i in languageList.indices) {
            if(languageList[i].locale == curLanguage) {
                temp = languageList[i]
                languageList.removeAt(i)
                break
            }
        }
        temp?.let{
            languageList.add(0, it)
        }
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@LanguageActivity)
        languageAdapter = LanguageItemAdapter(languageList) { selectedLanguage ->
            curLanguage = selectedLanguage.locale
            curLanguage?.let {
                languageAdapter.updateSelectedPosition(it)
            }
        }
        curLanguage?.let {
            languageAdapter.updateSelectedPosition(it)
        }
        binding.recyclerView.adapter = languageAdapter
    }

    private fun handleEvent() {
        binding.btnDone.setOnClickListener {
            curLanguage?.let {
                MyPreferences.write(MyPreferences.PREF_LANGUAGE, it)
                startActivity(Intent(this@LanguageActivity, HomeActivity::class.java))
                finish()
            }
        }
    }
}
