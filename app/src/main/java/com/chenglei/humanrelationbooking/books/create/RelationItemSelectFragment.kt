package com.chenglei.humanrelationbooking.books.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.*
import com.chenglei.humanrelationbooking.databinding.FragmentSelectRelationItemBinding
import com.chenglei.humanrelationbooking.databinding.LayoutRelationItemSelectBinding
import com.chenglei.humanrelationbooking.flowOb
import com.chenglei.humanrelationbooking.requireViewModel
import com.chenglei.humanrelationbooking.vms.RelationItem
import com.matisse.utils.dp2px
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class RelationItemSelectFragment:CommonBottomSheetDialogFragment() {
    private val relationListViewModel by lazy {
        requireViewModel<RelationItemsViewModel>()
    }

    private val createVM by lazy {
        requireActivity().requireViewModel<CreateViewModel>()
    }

    private val adapter by lazy {
        RelationItemAdapter {view, index, item ->
            createVM.updateRelationItem(item)
            dismiss()
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_select_relation_item
    }

    override fun bottomSheetConfig(): BottomSheetDialogConfig? {
        return defaultConfig().apply {
            peekHeight = dp2px(requireContext(), 100f)
        }
    }

    override fun initViews(binding: ViewDataBinding) {
        (binding as? FragmentSelectRelationItemBinding)?.let { binding->
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        flowOb(relationListViewModel.listFlow) {
            adapter.submitData(it)
        }

        relationListViewModel.load("")
    }
}

class RelationItemAdapter(
    val itemClick : (View, Int, RelationItem) -> Unit
) : PagingDataAdapter<RelationItem, RelationItemViewHolder>(object :
    DiffUtil.ItemCallback<RelationItem>() {
    override fun areItemsTheSame(oldItem: RelationItem, newItem: RelationItem): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: RelationItem, newItem: RelationItem): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onBindViewHolder(holder: RelationItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.render(it, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelationItemViewHolder {
        return RelationItemViewHolder(
            LayoutRelationItemSelectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),
            itemClick
        )
    }
}


class RelationItemViewHolder(binding: LayoutRelationItemSelectBinding,
itemClick: (View, Int, RelationItem) -> Unit
) : BindingViewHolder<RelationItem, LayoutRelationItemSelectBinding>(binding, itemClick) {
    override fun render(item: RelationItem, position: Int) {
        binding.item = item
        binding.clicker = View.OnClickListener {
            itemClick.invoke(it, position, item)
        }
    }

}


class RelationItemsViewModel: ListViewModel<Any,RelationItem>() {
    override suspend fun fetch(pageData: PageData<Any>): ListMeta<RelationItem> {
//        val query = BmobQuery<RelationItem>()
//            .addWhereEqualTo("owner", BmobUser.getCurrentUser().objectId)
//            .order("-createdAt")
//            .setLimit(pageData.pageSize)
//            .setSkip(pageData.pageNo * pageData.pageSize)
//        val items = query.findObjectsSync(RelationItem::class.java)
        return ListMeta(emptyList(),false)
    }
}