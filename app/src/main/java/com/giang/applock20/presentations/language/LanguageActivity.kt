package com.giang.applock20.presentations.language

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivityLanguageBinding
import com.giang.applock20.models.Language

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var context: Context
    private lateinit var resources: Resources


    val languageList = listOf(
        Language(getString(R.string.english), R.drawable.english_ic),
        Language(getString(R.string.korean), R.drawable.korean_ic),
        Language(getString(R.string.portuguese), R.drawable.portugal_ic),
        Language(getString(R.string.spanish), R.drawable.spanish_ic),
        Language(getString(R.string.japanese), R.drawable.japanese_ic),
        Language(getString(R.string.german), R.drawable.german_ic),
        Language(getString(R.string.polish), R.drawable.polish_ic),
        Language(getString(R.string.italian), R.drawable.italian_ic),
        Language(getString(R.string.french), R.drawable.french_ic),
        Language(getString(R.string.hindi), R.drawable.hindi_ic),
        Language(getString(R.string.vietnamese), R.drawable.vietnamese_ic)

    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language)
        binding.apply {
            val languagerv = recyclerView
            languagerv.layoutManager = LinearLayoutManager(this@LanguageActivity)
            languagerv.adapter = LanguageItemAdapter(
                languageList,
            ) { selectedLanguage: Language ->
                languageItemClicked(selectedLanguage)
            }
        }
    }

    private fun languageItemClicked(language: Language){
        val result = when (language.name) {
            "english" -> "en"
            "vietnamese" -> "vi"
            else -> "en"
        }
        context = LocaleHelper.setLocale(this, result)
        resources = context.resources
    }
}