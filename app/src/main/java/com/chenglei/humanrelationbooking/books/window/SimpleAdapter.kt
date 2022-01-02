package com.chenglei.humanrelationbooking.books.window

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.chenglei.humanrelationbooking.getString


class SimpleWindowItem(
    @StringRes val titleRes: Int,
    val click:View.OnClickListener,
)
class SimpleAdapter: RecyclerView.Adapter<SimpleViewHolder>() {
    val items = arrayListOf<SimpleWindowItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.render(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submit(data:List<SimpleWindowItem>){
        items.apply {
            clear()
            addAll(data)
        }
        this.notifyDataSetChanged()
    }
}


class SimpleViewHolder(container:View):RecyclerView.ViewHolder(container){

    private val tv by lazy {
        container.findViewById<TextView>(android.R.id.text1)
    }

    fun render(simpleWindowItem: SimpleWindowItem) {
        tv.text = getString(simpleWindowItem.titleRes)
        itemView.setOnClickListener(simpleWindowItem.click)
    }

}