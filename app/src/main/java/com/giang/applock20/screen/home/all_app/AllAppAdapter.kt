package com.giang.applock20.screen.home.all_app

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.giang.applock20.R
import com.giang.applock20.databinding.ItemAppBinding
import com.giang.applock20.model.AppInfo
import com.giang.applock20.util.AppInfoUtil

class AllAppAdapter(
    val context: Context,
    var appList: List<AppInfo>,
    private val onItemClick: (AppInfo, Int) -> Unit
) : RecyclerView.Adapter<AllAppAdapter.AppItemViewHolder>() {

    private lateinit var itemView: ItemAppBinding
    val mapSelectedApp = HashMap<String, Boolean>()

    fun updateSelectedPosition(selectedAppInfo: AppInfo, position: Int) {
        if (mapSelectedApp[selectedAppInfo.packageName] == null) {
            mapSelectedApp.put(selectedAppInfo.packageName, true)
        } else {
            mapSelectedApp.remove(selectedAppInfo.packageName)
        }
        notifyDataSetChanged()
        notifyItemChanged(position)
    }

    fun updateAllPosition(isSelected: Boolean) {
        if (isSelected) {
            appList.forEach {
                mapSelectedApp.put(it.packageName, true)
            }
        } else {
            mapSelectedApp.clear()
        }
        notifyDataSetChanged()
    }

    fun setNewList(newList: List<AppInfo>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = appList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return appList[oldItemPosition].packageName ==
                        newList[newItemPosition].packageName
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return appList[oldItemPosition].packageName == newList[newItemPosition].packageName
            }
        })

        appList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        itemView = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        holder.itemView.translationX = 0f
        val app = appList[position]
        holder.bind(app, position)
    }

    override fun getItemCount(): Int = appList.size

    inner class AppItemViewHolder(private val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppInfo, position: Int) {
            Log.e("Giang", "bind: ${app.name} - ${app.packageName}")
            Log.e("Giang", "bind: ${mapSelectedApp[app.packageName]}")
            binding.apply {
                imgAppIcon.setImageDrawable(AppInfoUtil.getAppIcon(context, app.packageName))
                tvAppName.text = app.name
                tvAppName.setTextColor(
                    if (mapSelectedApp[app.packageName] != null) Color.parseColor("#FFFFFF") else Color.parseColor("#131936")
                )
                itemView.setBackgroundResource(
                    if (mapSelectedApp[app.packageName] != null) R.drawable.bg_selected_language_item else R.drawable.bg_language_item
                )
                // Global debounce to prevent multiple clicks across items
                itemView.setOnClickListener {
                    onItemClick(app, position)
                }
            }
        }
    }
}

class SlideOutRightItemAnimator : DefaultItemAnimator() {
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        Log.e("GiangAnCut", "animateRemove: ${holder.itemView}")
        val view = holder.itemView
        view.animate()
            .translationX(view.width.toFloat() + 100f)
            .setDuration(300)
            .withEndAction {
                dispatchRemoveFinished(holder)
            }
            .start()
        return true
    }

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        Log.e("GiangAnCut", "animateAppearance: ${viewHolder.itemView}")
        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo)
    }

    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        Log.e("GiangAnCut", "animateDisappearance: ${viewHolder.itemView}")
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo)
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        Log.e("GiangAnCut", "animateChange: ${oldHolder.itemView}")
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        Log.e("GiangAnCut", "animateAdd: ${holder.itemView}")
        val view = holder.itemView
        view.translationX = view.width.toFloat()
        view.animate()
            .translationX(0f)
            .setDuration(300)
            .withEndAction {
                dispatchAddFinished(holder)
            }
            .start()
        return true
    }
}