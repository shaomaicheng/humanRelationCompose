package com.chenglei.humanrelationbooking.debug
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.databinding.DebugDevelopBinding
import com.chenglei.humanrelationbooking.dialog.LoadingDialog
import com.chenglei.humanrelationbooking.launchDialog
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.ToolbarConfig
import com.hangshun.huadian.android.common.arch.toolbarConfig

 class DevelopActivity:ActivityBase() {

    private val devAdapter by lazy {
        DevAdapter{view,p, item->
            when (item.type) {
                DevItemType.Logout-> {
                    BmobUser.logOut()
                }

                DevItemType.Loading -> {
                    this.launchDialog(LoadingDialog::class.java)
                }

                DevItemType.Flow->{
                    startActivity(Intent(this, FlowDebugActivity::class.java))
                }
            }
        }
    }

    override fun needToolBar(): Boolean {
        return true
    }

    private lateinit var binding: DebugDevelopBinding

    override fun toolBarConfig(): ToolbarConfig? {
        return toolbarConfig {
            color = R.color.white
            navigatorColor=R.color.black
            textColor = R.color.black
            title = getString(R.string.debug_paging_title)
        }
    }

    override fun realContentView(): View {
        binding = DebugDevelopBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        binding.recyclerView.apply {
            this.adapter = devAdapter
            this.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }
        devAdapter.submit(arrayListOf(
            DevItem(DevItemType.Logout,"退出登录"),
            DevItem(DevItemType.Loading,"加载对话框"),
            DevItem(DevItemType.Flow,"flow")
        ))
    }

     data class DevItem(
        val type: DevItemType,
        val title: String
    )

     enum class DevItemType {
        Logout,Loading, Flow
    }

    inner class DevAdapter(val clickListener: (View,Int,DevItem)->Unit) : RecyclerView.Adapter<DevVH>() {
        private val data = mutableListOf<DevItem>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevVH {
            return DevVH(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
        }


        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: DevVH, position: Int) {
            holder.render(data[position]) {
                clickListener.invoke(it, position, data[position])
            }
        }

        fun submit(list: List<DevItem>) {
            data.apply {
                this.clear()
                this.addAll(list)
            }
            notifyDataSetChanged()
        }

    }

    inner class DevVH(itemView:View) : RecyclerView.ViewHolder(itemView) {

        private val text by lazy {
            itemView.findViewById<TextView>(android.R.id.text1)
        }

        fun render(value:DevItem, clickListener: View.OnClickListener) {
            text.text = value.title
            text.setOnClickListener(clickListener)
        }
    }
}