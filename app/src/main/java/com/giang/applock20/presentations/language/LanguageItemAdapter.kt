package com.giang.applock20.presentations.language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.giang.applock20.R
import com.giang.applock20.models.Language

class LanguageItemAdapter(
    private val languageList: List<Language>,
    private val clickListener:(Language) -> Unit) : RecyclerView.Adapter<LanguageItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.language_item, parent, false)
        return LanguageItemViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: LanguageItemViewHolder, position: Int) {
        val language = languageList[position]
        holder.bind(language, clickListener)
    }

    override fun getItemCount(): Int = languageList.size
}

class LanguageItemViewHolder (val view: View): RecyclerView.ViewHolder(view){

    fun bind(language : Language, clickListener:(Language) -> Unit) {
        val myTextView = view.findViewById<TextView>(R.id.languageName)
        val myImageView = view.findViewById<ImageView>(R.id.languageIcon)
        myTextView.text = language.name
        myImageView.setImageResource(language.icon)

//        view.setOnClickListener{
//            clickListener(language)
//            val languageItemBg = view.findViewById<View>(R.id.languageItemBg)
//            val tvLanguageName = view.findViewById<TextView>(R.id.languageName)
//
//            languageItemBg.setBackgroundResource(R.drawable.langauge_item_selected_bg)
//            tvLanguageName.setTextColor(ContextCompat.getColor(view.context, R.color.white))
//        }
    }

}