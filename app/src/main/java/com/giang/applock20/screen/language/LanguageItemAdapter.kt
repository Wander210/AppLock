package com.giang.applock20.screen.language

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giang.applock20.R
import com.giang.applock20.model.Language

class LanguageItemAdapter(
    private val languageList: List<Language>,
    private val clickListener: (Language) -> Unit
) : RecyclerView.Adapter<LanguageItemViewHolder>() {

    private var selectedPosition: Int = -1

    fun updateSelectedPosition(selectedLanguage: String) {
        val previousPosition = selectedPosition
        for(i in languageList.indices) {
            if(languageList[i].locale == selectedLanguage) {
                selectedPosition = i
                break
            }
        }

        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageItemViewHolder, position: Int) {
        val language = languageList[position]
//        curLanguage?.let {
//            updateSelectedPosition(it)
//        }
        holder.bind(language, position == selectedPosition, clickListener)
    }

    override fun getItemCount(): Int = languageList.size
}

class LanguageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivLanguageIcon: ImageView = itemView.findViewById(R.id.languageIcon)
    private val tvLanguageName: TextView = itemView.findViewById(R.id.languageName)

    fun bind(language: Language, isSelected: Boolean, onLanguageSelected: (Language) -> Unit) {
        ivLanguageIcon.setImageResource(language.icon)
        tvLanguageName.text = language.name
        tvLanguageName.setTextColor(
            if(isSelected) Color.parseColor("#FFFFFF") else Color.parseColor("#131936")
        )
        itemView.setBackgroundResource(
            if (isSelected) R.drawable.language_item_selected_bg else R.drawable.language_item_bg
        )
        itemView.setOnClickListener { onLanguageSelected(language) }
    }
}
