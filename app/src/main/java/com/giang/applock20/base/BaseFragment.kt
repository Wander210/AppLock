package com.giang.applock20.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    protected lateinit var binding: VB
    protected var recyclerViewAdapter: RecyclerView.Adapter<*>? = null

    abstract fun getViewBinding(layoutInflater: LayoutInflater) : VB

    abstract fun initData()

    abstract fun setupView()

    abstract fun handleEvent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        setupView()
        handleEvent()
    }
}

