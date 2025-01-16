package com.giang.applock20.screen.language

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageItemViewHolder, position: Int) {
        val language = languageList[position]
        holder.bind(language, position == selectedPosition) { selectedLanguage ->

            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition) 
            notifyItemChanged(position) 
            clickListener(selectedLanguage) 
        }
    }

    override fun getItemCount(): Int = languageList.size
}

class LanguageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivLanguageIcon: ImageView = itemView.findViewById(R.id.languageIcon)
    private val txtLanguageName: TextView = itemView.findViewById(R.id.languageName)

    fun bind(language: Language, isSelected: Boolean, onLanguageSelected: (Language) -> Unit) {
        ivLanguageIcon.setImageResource(language.icon)
        txtLanguageName.text = language.name
        itemView.setBackgroundResource(
            if (isSelected) R.drawable.language_item_selected_bg else R.drawable.language_item_bg
        )
        itemView.setOnClickListener { onLanguageSelected(language) }
    }
}
