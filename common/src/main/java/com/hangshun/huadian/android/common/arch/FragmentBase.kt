package com.hangshun.huadian.android.common.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class FragmentBase : Fragment() {
    protected var binding:ViewDataBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = if (dataBinding()) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), container, false)
            binding!!.root
        } else {
            LayoutInflater.from(context).inflate(layoutId(), container, false)
        }
        try {
            initViews(root)
        } catch (e:Exception){
            e.printStackTrace()
        }
        return root
    }

    @LayoutRes abstract fun layoutId(): Int
    abstract fun initViews(root:View)
    open fun dataBinding():Boolean = false
}