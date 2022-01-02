package com.chenglei.humanrelationbooking.books

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chenglei.humanrelationbooking.BuildConfig
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ComposeActivity
import com.chenglei.humanrelationbooking.base.LoadingAdapter
import com.chenglei.humanrelationbooking.books.create.BookItemType
import com.chenglei.humanrelationbooking.books.create.RecordCreateActivity
import com.chenglei.humanrelationbooking.books.vms.BooksHomeViewModel
import com.chenglei.humanrelationbooking.books.window.SimpleAdapter
import com.chenglei.humanrelationbooking.books.window.SimpleWindowItem
import com.chenglei.humanrelationbooking.compose.home.Hosts
import com.chenglei.humanrelationbooking.databinding.FragmentBooksBinding
import com.chenglei.humanrelationbooking.debug.DevelopActivity
import com.chenglei.humanrelationbooking.flowOb
import com.chenglei.humanrelationbooking.requireViewModel
import com.chenglei.humanrelationbooking.utils.getCompatColor
import com.hangshun.huadian.android.common.arch.FragmentBase
import com.hangshun.huadian.android.common.drawable.configDrawable
import com.hangshun.huadian.android.common.utils.dp2px
import kotlinx.coroutines.FlowPreview


class BooksFragment : FragmentBase() {

    private val booksViewModel by lazy {
        requireViewModel<BooksHomeViewModel>()
    }

    private val booksItemsViewModel by lazy {
        requireViewModel<BookRecordListViewModel>()
    }

    private val vm by lazy { requireViewModel<BooksViewModel>() }


    private val adapter by lazy {
        BookItemAdapter { item, position ->
            RecordCreateActivity.launchEdit(requireActivity(), item)
        }
    }

    override fun layoutId() = R.layout.fragment_books


    override fun dataBinding(): Boolean {
        return true
    }


    val popupWindow by lazy {

        val popupWindow = PopupWindow(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow.contentView =  ConstraintLayout(
            requireContext()
        ).apply {
            val list = RecyclerView(requireContext()).apply {
                this.adapter = SimpleAdapter().apply {
                    submit(listOf(
                        SimpleWindowItem(
                            R.string.index_add_book
                        ) {
                            ComposeActivity.launch(requireContext(), host = Hosts.AddBook)
                            popupWindow.takeIf { it.isShowing }?.dismiss()
                        },
                        SimpleWindowItem(
                            R.string.index_add_book_item
                        ) {
                            RecordCreateActivity.launchCreate(requireContext())
                            popupWindow.takeIf { it.isShowing }?.dismiss()
                        }
                    ))
                }
                this.layoutManager = LinearLayoutManager(requireContext())
            }
            val padding = dp2px(requireContext(),3f)
            addView(list.apply { setPadding(padding,padding,padding,padding) }, ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                startToStart = 0
                endToEnd=0
                topToTop = 0
                bottomToBottom = 0
            })

        }
        popupWindow.setBackgroundDrawable(configDrawable(requireContext()){
            solid = getCompatColor(R.color.window_bg)
            radius = 8f
        })
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow
    }

    @FlowPreview
    override fun initViews(root: View) {

        flowOb(booksViewModel.expend) { expand ->
            (binding as? FragmentBooksBinding)?.let { binding ->
                binding.booksList.isVisible = !expand
            }
        }
        flowOb(booksItemsViewModel.listFlow) {
            adapter.submitData(it)
            (binding as? FragmentBooksBinding)?.let {
                it.booksList.postDelayed({
                    it.booksList.scrollToPosition(0)
                }, 1000)
            }
        }
        flowOb(booksViewModel.currentBook) {
            it?.apply {
                if (this.queryType == 0) {
                    this.queryType = booksViewModel.type.value.type
                }
                booksItemsViewModel.load(this)
            }
        }
        flowOb(booksViewModel.type) {
            val param = booksViewModel.currentBook.value?.apply {
                queryType = it.type
            }
            param?.apply {
                booksItemsViewModel.load(this)
            }
        }



        (binding as? FragmentBooksBinding)?.let { binding ->
            binding.lifecycleOwner = viewLifecycleOwner
            binding.booksVM = booksViewModel
            binding.clicker = View.OnClickListener {
                when (it.id) {
                    R.id.tvIncome -> {
                        booksViewModel.type.value = BookItemType.Income
                    }

                    R.id.tvSpend -> {
                        booksViewModel.type.value = BookItemType.Spend
                    }

                    R.id.title -> {
                        if (BuildConfig.DEBUG) {
//                            startActivity(Intent(requireActivity(), FlowDebugActivity::class.java))
//                            startActivity(Intent(requireActivity(), PagingDebugActivity::class.java))
                            startActivity(Intent(requireActivity(), DevelopActivity::class.java))
                        }
                    }

                    R.id.addBookItem -> {
                        context?.let { context ->
                            popupWindow.showAsDropDown(it,-(dp2px(context,70f)) ,0)
                        }
                    }
                }
            }
            binding.booksList.adapter = withLoadStateFooter(
                adapter,
                footer = LoadingAdapter {},
                refreshFooter = LoadingAdapter {}
            )

            binding.booksList.layoutManager = LinearLayoutManager(requireContext())
            binding.swipeRefresh.setOnRefreshListener {
                booksItemsViewModel.refresh()
            }
            binding.swipeRefresh.setColorSchemeResources(R.color.primary_500)

            adapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.Error,
                    is LoadState.NotLoading -> {
                        binding.swipeRefresh.isRefreshing = false
                    }
                    else -> {}
                }
            }
        }
        BooksBlockHolder(this, root, binding as? FragmentBooksBinding)
    }


    fun boosLoading(isLoading: Boolean) {
        (binding as? FragmentBooksBinding)?.booksBlock?.isLoading = isLoading
    }

    private fun withLoadStateFooter(
        source: PagingDataAdapter<*, *>,
        footer: LoadStateAdapter<*>,
        refreshFooter: LoadStateAdapter<*>
    ): ConcatAdapter {
        source.addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
            refreshFooter.loadState = loadStates.refresh
        }
        return ConcatAdapter(source, footer)
    }

}