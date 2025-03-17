package com.giang.applock20.screen.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.giang.applock20.screen.home.all_app.AllAppFragment
import com.giang.applock20.screen.home.locked_app.LockedAppFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2


    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            AllAppFragment()
        else
            LockedAppFragment()
    }

}