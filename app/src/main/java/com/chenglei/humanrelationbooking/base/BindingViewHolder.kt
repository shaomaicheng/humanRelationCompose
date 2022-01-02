package com.chenglei.humanrelationbooking.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindingViewHolder<T,BINDING:ViewDataBinding>(
    val binding:BINDING,
    val itemClick:(View,Int,T)->Unit
    ):RecyclerView.ViewHolder(binding.root) {
    abstract fun render(item:T, position: Int)
}