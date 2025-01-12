package com.giang.applock20.presentations.language

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
    val languageList = listOf(
        Language("English", R.drawable.english_ic),
        Language("Korean", R.drawable.korean_ic),
        Language("Portugal", R.drawable.portugal_ic),
        Language("Spanish", R.drawable.spanish_ic),
        Language("Japanese", R.drawable.japanese_ic),
        Language("German", R.drawable.german_ic),
        Language("Polish", R.drawable.polish_ic),
        Language("Italian", R.drawable.italian_ic),
        Language("French", R.drawable.french_ic),
        Language("Hindi", R.drawable.hindi_ic),
        Language("VIetnamese", R.drawable.vietnamese_ic)

    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language)
        binding.apply {
            val languagerv = recyclerView
            languagerv.layoutManager = LinearLayoutManager(this@LanguageActivity)
            languagerv.adapter = LanguageItemAdapter(languageList)

        }
    }
}