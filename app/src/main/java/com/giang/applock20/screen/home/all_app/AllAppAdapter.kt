package com.giang.applock20.screen.home.all_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.giang.applock20.R
import com.giang.applock20.databinding.ItemAppBinding
import com.giang.applock20.model.AppInfo
import com.giang.applock20.util.AppInfoUtil.listAppInfo

class AllAppAdapter(
    var appList: List<AppInfo>,
    private val onItemClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<AllAppAdapter.AppItemViewHolder>() {

    private lateinit var itemView: ItemAppBinding
    internal var booleanArray = BooleanArray(appList.size)
    internal var count = 0
    private var lastClickTime = 0L

    fun updateSelectedPosition(selectedAppInfo: AppInfo) {
        for (i in appList.indices) {
            if (appList[i].packageName == selectedAppInfo.packageName) {
                if (!booleanArray[i]) {
                    booleanArray[i] = true
                    count++
                } else {
                    booleanArray[i] = false
                    count--
                }
                notifyItemChanged(i)
                break
            }
        }
    }

    fun updateAllPosition(isSelected: Boolean) {
        for (i in booleanArray.indices) {
            if (isSelected) booleanArray[i] = true
            else booleanArray[i] = false
            notifyItemChanged(i)
        }
        if (isSelected) count = booleanArray.size
        else count = 0
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
                return appList[oldItemPosition] == newList[newItemPosition]
            }
        })

        appList = newList
        booleanArray = BooleanArray(appList.size)
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
            binding.apply {
                imgAppIcon.setImageDrawable(app.icon)
                tvAppName.text = app.name
                tvAppName.setTextColor(
                    if (booleanArray[position]) Color.parseColor("#FFFFFF") else Color.parseColor("#131936")
                )
                itemView.setBackgroundResource(
                    if (booleanArray[position]) R.drawable.bg_selected_language_item else R.drawable.bg_language_item
                )
                // Global debounce to prevent multiple clicks across items
                itemView.setOnClickListener {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime > 800) {
                        lastClickTime = currentTime
                        onItemClick(app)
                    }
                }
            }
        }
    }
}

class SlideOutRightItemAnimator : DefaultItemAnimator() {
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val view = holder.itemView
        view.animate()
            .translationX(view.width.toFloat())
            .setDuration(300)
            .withEndAction {
                dispatchRemoveFinished(holder)
            }
            .start()
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
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