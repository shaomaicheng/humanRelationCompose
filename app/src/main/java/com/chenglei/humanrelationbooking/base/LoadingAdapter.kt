package com.chenglei.humanrelationbooking.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chenglei.humanrelationbooking.databinding.LayoutListStatusBinding

class ListStatusViewHolder(
    val binding: LayoutListStatusBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun render(state: LoadState) {
        when (state) {
            is LoadState.Loading->{
                binding.loading = true
                binding.error = false
                binding.noMore = false

            }
            is LoadState.Error -> {
                binding.loading = false
                binding.error = true
                binding.noMore = false
            }

            is LoadState.NotLoading -> {
                binding.loading = false
                binding.error = false
                binding.noMore = true
            }
        }
    }
}

class LoadingAdapter(private val retry: () -> Unit) : LoadStateAdapter<ListStatusViewHolder>() {
    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState) || loadState is LoadState.NotLoading
    }

    override fun onBindViewHolder(holder: ListStatusViewHolder, loadState: LoadState) {
        holder.render(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ListStatusViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListStatusViewHolder(LayoutListStatusBinding.inflate(inflater,parent,false), retry)
    }
}