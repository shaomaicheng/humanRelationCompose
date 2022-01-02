package com.chenglei.humanrelationbooking.books.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.ActivityConst
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.*
import com.chenglei.humanrelationbooking.databinding.FragmentBookListBinding
import com.chenglei.humanrelationbooking.databinding.LayoutSimpleBookItemBinding
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.requireViewModel
import com.google.gson.reflect.TypeToken
import com.matisse.utils.dp2px
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
账本列表
 */
class BookListFragment : CommonBottomSheetDialogFragment() {

    private val bookListViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val books = arguments?.getParcelableArrayList(ActivityConst.KEY_BOOKS) ?: emptyList<Book>()
                return modelClass.getConstructor(
                    object:TypeToken<List<Book>>(){}.rawType
                ).newInstance(books)
            }

        })[BookListViewModel::class.java]
    }

    @InternalCoroutinesApi
    private val adapter by lazy {
        BookSimpleAdapter {item,itemView->
            val createViewModel = activity?.requireViewModel<CreateViewModel>()
            createViewModel?.chooseBook(item)
            dialog?.dismiss()
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_book_list
    }

    override fun bottomSheetConfig(): BottomSheetDialogConfig? {
        return defaultConfig().apply {
            peekHeight = dp2px(requireContext(), 100f)
        }
    }

    @InternalCoroutinesApi
    @FlowPreview
    override fun initViews(binding: ViewDataBinding) {
        (binding as? FragmentBookListBinding)?.let { binding ->


            binding.recyclerView.adapter = adapter
            viewLifecycleOwner.lifecycleScope.launch {
                bookListViewModel.listFlow
                    .collectLatest {
                        adapter.submitData(it)
                    }
            }
            bookListViewModel.load("")

        }
    }
}

class BookSimpleAdapter(private val click:(Book,View)->Unit): PagingDataAdapter<Book, BookSimpleViewHolder>(
    object : DiffUtil.ItemCallback<Book>(){
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onBindViewHolder(holder: BookSimpleViewHolder, position: Int) {
        getItem(position)?.apply {  holder.render(this)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSimpleViewHolder {
        return BookSimpleViewHolder(
            LayoutSimpleBookItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            click
        )
    }

}

class BookSimpleViewHolder(private val binding:LayoutSimpleBookItemBinding,
                            private val click:(Book,View)->Unit):RecyclerView.ViewHolder(binding.root){

    fun render(item:Book){
        binding.name = item.name
        binding.clicker = View.OnClickListener {
            click.invoke(item,it)
        }
    }
}

class BookListViewModel(val books: List<Book>) : ListViewModel<String, Book>() {
    override suspend fun fetch(pageData: PageData<String>): ListMeta<Book> {
        return if (books.isNotEmpty()) {
            ListMeta(books, false)
        }else{
            val remotes = BmobQuery<Book>().addWhereEqualTo("userId", BmobUser.getCurrentUser().objectId)
                .findObjectsSync(Book::class.java)
            ListMeta(remotes, false)
        }
    }

}
