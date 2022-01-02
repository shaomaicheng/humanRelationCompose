package com.chenglei.humanrelationbooking.books.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.*
import com.chenglei.humanrelationbooking.databinding.FragmentSelectRelationBinding
import com.chenglei.humanrelationbooking.databinding.LayoutItemRelationSelectBinding
import com.chenglei.humanrelationbooking.requireViewModel
import com.chenglei.humanrelationbooking.utils.getString
import com.matisse.utils.dp2px
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class RelationSelectFragment : CommonBottomSheetDialogFragment() {

    private val relationSelectViewModel by lazy {
        requireViewModel<RelationSelectViewModel>()
    }


    private val createViewModel by lazy {
        requireActivity().requireViewModel<CreateViewModel>()
    }

    private val adapter by lazy {
        RelationSelectAdapter { view, i, s ->

            createViewModel.chooseRelation(s)
            dismiss()
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_select_relation
    }

    override fun bottomSheetConfig(): BottomSheetDialogConfig? {
        return defaultConfig().apply {
            peekHeight = dp2px(requireContext(), 100f)
        }
    }

    override fun initViews(binding: ViewDataBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            relationSelectViewModel.listFlow.collect {
                adapter.submitData(it)
            }
        }
        (binding as? FragmentSelectRelationBinding)?.let { binding ->
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        relationSelectViewModel.load("")
    }
}

class RelationSelectAdapter(val itemClick: (View, Int, String) -> Unit) :
    PagingDataAdapter<String, RelationSelectViewHolder>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onBindViewHolder(holder: RelationSelectViewHolder, position: Int) {
        getItem(position)?.let { holder.render(it, position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelationSelectViewHolder {
        return RelationSelectViewHolder(
            LayoutItemRelationSelectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), itemClick
        )
    }

}

class RelationSelectViewHolder(
    binding: LayoutItemRelationSelectBinding,
    itemClick: (View, Int, String) -> Unit
) : BindingViewHolder<String, LayoutItemRelationSelectBinding>(binding, itemClick) {
    override fun render(item: String, position: Int) {
        binding.item = item
        binding.root.setOnClickListener {
            itemClick.invoke(it, position, item)
        }
    }

}


class RelationSelectViewModel : ListViewModel<String, String>() {
    override suspend fun fetch(pageData: PageData<String>): ListMeta<String> {
        return ListMeta(RelationDataSource().relationConfigs(), false)
    }
}

class RelationDataSource {
    fun relationConfigs(): List<String> = listOf(
        getString(R.string.relation_male),
        getString(R.string.relation_female),
        getString(R.string.relation_relatives),
        getString(R.string.relation_classmates),
        getString(R.string.relation_friend),
        getString(R.string.relation_colleagues),
        getString(R.string.relation_teacher),
        getString(R.string.relation_student),
        getString(R.string.relation_leader)
    )
}