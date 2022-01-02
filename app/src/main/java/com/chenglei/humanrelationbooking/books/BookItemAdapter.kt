package com.chenglei.humanrelationbooking.books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chenglei.humanrelationbooking.applog
import com.chenglei.humanrelationbooking.databinding.LayoutItemBookRecordBinding
import com.chenglei.humanrelationbooking.meta.BookItem
import java.text.SimpleDateFormat
import java.util.*

class BookItemAdapter(private val itemClick:(BookItem, Int)->Unit): PagingDataAdapter<BookItem,BookItemViewHolder>(
    object : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem.objectId == newItem.objectId
        }

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        applog("onbindviewholder: $position")
        getItem(position)?.apply {
            holder.render(this, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(LayoutItemBookRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false), itemClick)
    }
}

class BookItemViewHolder(val binding: LayoutItemBookRecordBinding,
val itemClick : (BookItem, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    fun render(item: BookItem, position: Int) {
        binding.item = item
        binding.position = position
        binding.clicker = View.OnClickListener {
            itemClick.invoke(item, position)
        }
    }
}

@BindingAdapter("bookItemDate")
fun bookItemDate(tv:TextView, time:Long) {
    tv.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(time)
}