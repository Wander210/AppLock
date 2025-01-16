package com.giang.applock20.screen.language

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivityLanguageBinding
import com.giang.applock20.model.Language
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.util.LanguageUtil

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var languageAdapter: LanguageItemAdapter
    private var curLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyPreferences.init(this)
        curLanguage = MyPreferences.read(MyPreferences.PREF_LANGUAGE, "en")
        enableEdgeToEdge()

        val languageList = listOf(
            Language(0, getString(R.string.english), R.drawable.english_ic, curLanguage == "en"),
            Language(1, getString(R.string.korean), R.drawable.korean_ic, curLanguage == "kr"),
            Language(2, getString(R.string.portuguese), R.drawable.portugal_ic, curLanguage == "pt"),
            Language(3, getString(R.string.spanish), R.drawable.spanish_ic, curLanguage == "es"),
            Language(4, getString(R.string.japanese), R.drawable.japanese_ic, curLanguage == "ja"),
            Language(5, getString(R.string.german), R.drawable.german_ic, curLanguage == "de"),
            Language(6, getString(R.string.polish), R.drawable.polish_ic, curLanguage == "pl"),
            Language(7, getString(R.string.italian), R.drawable.italian_ic, curLanguage == "it"),
            Language(8, getString(R.string.french), R.drawable.french_ic, curLanguage == "fr"),
            Language(9, getString(R.string.hindi), R.drawable.hindi_ic, curLanguage == "hi"),
            Language(10, getString(R.string.vietnamese), R.drawable.vietnamese_ic, curLanguage == "vi")
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_language)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@LanguageActivity)
            languageAdapter = LanguageItemAdapter(languageList) { selectedLanguage ->
                curLanguage = when (selectedLanguage.name) {
                    getString(R.string.english) -> "en"
                    getString(R.string.korean) -> "kr"
                    getString(R.string.portuguese) -> "pt"
                    getString(R.string.spanish) -> "es"
                    getString(R.string.japanese) -> "ja"
                    getString(R.string.german) -> "de"
                    getString(R.string.polish) -> "pl"
                    getString(R.string.italian) -> "it"
                    getString(R.string.french) -> "fr"
                    getString(R.string.hindi) -> "hi"
                    getString(R.string.vietnamese) -> "vi"
                    else -> "en"
                }
                languageList.forEach { it.isSelected = it.name == selectedLanguage.name }
                languageAdapter.notifyDataSetChanged()
            }
            recyclerView.adapter = languageAdapter

            btnDone.setOnClickListener {
                MyPreferences.write(MyPreferences.PREF_LANGUAGE, curLanguage!!)
                LanguageUtil.setLanguage(this@LanguageActivity)
                finish()
            }
        }
    }
}
