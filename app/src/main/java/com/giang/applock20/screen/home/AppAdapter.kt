package com.giang.applock20.screen.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giang.applock20.R
import com.giang.applock20.databinding.FragmentAllAppsBinding
import com.giang.applock20.databinding.ItemAppBinding
import com.giang.applock20.databinding.ItemLanguageBinding
import com.giang.applock20.model.AppInfo
import com.giang.applock20.screen.language.LanguageItemViewHolder

class AppAdapter(var appList: List<AppInfo>) :
    RecyclerView.Adapter<AppAdapter.AppItemViewHolder>() {

    private lateinit var itemView: ItemAppBinding

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList : List<AppInfo>) {
        appList = filteredList
        notifyDataSetChanged()
    }

    inner class AppItemViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppInfo) {
            binding.apply{
                imgAppIcon.setImageDrawable(app.icon)
                tvAppName.text = app.name
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        itemView = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        val app = appList[position]
        holder.bind(app)

    }

    override fun getItemCount(): Int = appList.size
}
