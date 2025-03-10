package com.giang.applock20.screen.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
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

    fun setFilteredList(filteredList: List<AppInfo>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = appList.size

            override fun getNewListSize(): Int = filteredList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return appList[oldItemPosition].packageName == filteredList[newItemPosition].packageName
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return appList[oldItemPosition] == filteredList[newItemPosition]
            }
        })

        appList = filteredList
        diffResult.dispatchUpdatesTo(this)
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
        holder.itemView.translationX = 0f

        val app = appList[position]
        holder.bind(app)

    }

    override fun getItemCount(): Int = appList.size

    inner class SlideOutRightItemAnimator : DefaultItemAnimator() {
        override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
            val view = holder.itemView
            view.animate()
                .translationX(-view.width.toFloat())
                .setDuration(800)
                .withEndAction {
                    dispatchRemoveFinished(holder)
                }
                .start()
            return true
        }

        override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
            val view = holder.itemView
            view.translationX = -view.width.toFloat()
            view.animate()
                .translationX(0f)
                .setDuration(800)
                .withEndAction {
                    dispatchAddFinished(holder)
                    
                }
                .start()
            return true
        }
    }

}
