package com.chenglei.humanrelationbooking.books

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.EventCenter
import com.chenglei.humanrelationbooking.books.create.RecordCreateActivity
import com.chenglei.humanrelationbooking.books.meta.BookUI
import com.chenglei.humanrelationbooking.books.vms.BooksHomeViewModel
import com.chenglei.humanrelationbooking.databinding.FragmentBooksBinding
import com.chenglei.humanrelationbooking.flowOb
import com.chenglei.humanrelationbooking.flowObLast
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.requireViewModel
import com.chenglei.humanrelationbooking.utils.getString
import com.chenglei.humanrelationbooking.utils.replace
import com.hangshun.huadian.android.common.drawable.configDrawable
import com.hangshun.huadian.android.common.utils.dp2px
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlin.concurrent.thread

class BooksBlockHolder(host: BooksFragment, root: View, binding: FragmentBooksBinding?) {

    private val booksContainer: ConstraintLayout by lazy {
        root.findViewById(R.id.books)
    }

    private val booksHomeViewModel by lazy {
        host.requireViewModel<BooksHomeViewModel>()
    }

    private val booksViewModel by
        host.requireActivity().viewModels<BooksViewModel> {
            ViewModelProvider.AndroidViewModelFactory(host.requireActivity().application)
        }


    private val adapter by lazy {
        BooksBlockAdapter(host, host.requireContext(),  {status->
            booksHomeViewModel.status.value = status
        }, {book->
            booksHomeViewModel.currentBook.value = book
        })
    }


    private val backgrounds = listOf(
        BookBackground(R.color.books_card1_start, R.color.books_card1_end, R.color.books_card1_arc),
        BookBackground(R.color.books_card2_start, R.color.books_card2_end, R.color.books_card2_arc),
        BookBackground(R.color.books_card3_start, R.color.books_card3_end, R.color.books_card3_arc),
        BookBackground(R.color.books_card4_start, R.color.books_card4_end, R.color.books_card4_arc),
        BookBackground(R.color.books_card5_start, R.color.books_card5_end, R.color.books_card5_arc),
        BookBackground(R.color.books_card6_start, R.color.books_card6_end, R.color.books_card6_arc),
    )

    init {
        host.flowOb(booksViewModel.books){books->
            host.boosLoading(false)
            val booksUis = booksViewModel.books.value.mapIndexed { index, book ->
                BookUI(book, backgrounds[index%backgrounds.size])
            }.filter {book->
                book.book.income != 0 || book.book.spend != 0
            }
            adapter.attach(booksContainer, booksUis)
            binding?.showEmpty = booksUis.isEmpty()
        }
        host.flowObLast(booksViewModel.loading){
                host.boosLoading(it)
        }

        EventCenter.observer<Book>("book", booksViewModel.viewModelScope) {
            it?.let {book ->
//                adapter.notifyItemRefresh(it)
                booksViewModel.getBooks()
            }
        }
        booksViewModel.getBooks()
    }
}

data class BookBackground(
    val startColor: Int,
    val endColor: Int,
    val arcColor: Int
)

class BooksBlockAdapter(val host:Fragment, val context: Context, val uiStatusChange:(BooksBlockStatus)->Unit,
    val bookSelected: (Book) -> Unit) {

    private var status: BooksBlockStatus = BooksBlockStatus.Expand
    private val data : MutableList<BookUI> = mutableListOf()

    private val holders = mutableListOf<BooksBlockViewHolder>()
    private var pending = false

    private var collectAnimationSize = holders.size

    fun notifyItemRefresh(book:Book) {
        data.forEachIndexed { index, bookUI ->
//            if (bookUI.book.objectId == book.objectId) {
//                bookUI.book = book
//                holders[index].refreshData(bookUi = bookUI)
//            }
        }
    }

    fun attach(container: ConstraintLayout, bookUis: List<BookUI>) {
        data.replace(bookUis)
        container.removeAllViews()
        status = BooksBlockStatus.Expand
        holders.clear()
        pending = false
        collectAnimationSize = 0
        data.forEachIndexed { index, book ->
            val viewHolder = createViewHolder(container, {
                collectAnimationSize -= 1
            }, { position ->
                // position: 需要弄成最后一个的下标
                thread {
                    while (true) {
                        synchronized(collectAnimationSize) {
                            if (collectAnimationSize == 0) {
                                uiStatusChange.invoke(status)
                                Handler(Looper.getMainLooper()).post {
                                    holders.forEach { booksBlockViewHolder ->
                                        val index = booksBlockViewHolder.uiPosition
                                        if (index == position) {
                                            booksBlockViewHolder.back(holders.size - 1)
                                        } else if (index in position until holders.size) {
                                            booksBlockViewHolder.font()
                                        }
                                    }
                                }
                                collectAnimationSize = holders.size
                                return@thread
                            }
                        }
                    }
                }
                (context as? FragmentActivity)?.let { activity->
                    host.requireViewModel<BooksHomeViewModel>().expend.value = false
                }
            }, expandCallback = {
                (context as? FragmentActivity)?.let { activity->
                    host.requireViewModel<BooksHomeViewModel>().expend.value = true
                }
            })
            bindViewHolder(viewHolder, index)
            holders.add(viewHolder)
            collectAnimationSize = holders.size
        }
    }

    private fun createViewHolder(
        parent: ViewGroup,
        collectSingleFinish: () -> Unit,
        collectCallback: (Int) -> Unit,
        expandCallback:(()->Unit)? = null
    ): BooksBlockViewHolder {
        val cardView = LayoutInflater.from(context).inflate(
            R.layout.layout_books_item,
            parent, false
        )
        parent.addView(cardView)
        return BooksBlockViewHolder(cardView, maxSize = data.size) { position, dataPosition ->
            if (pending) return@BooksBlockViewHolder
            pending = true
            status = if (status == BooksBlockStatus.Expand) {
                this.bookSelected.invoke(data[dataPosition].book)
                // 大家的margintop都逐步变成0
                holders.forEach {
                    it.toCollect(position, {
                        collectSingleFinish()
                    }) { position ->
                        collectCallback.invoke(position)
                        pending = false
                    }
                }
                BooksBlockStatus.Collect
            } else {
                val animators = holders.map { it.toExpand()}
                AnimatorSet().apply {
                    playTogether(animators)
                    duration = 1000
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            expandCallback?.invoke()
                            pending = false
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })
                }.start()
                BooksBlockStatus.Expand
            }

            if (status == BooksBlockStatus.Expand) {
                uiStatusChange.invoke(status)
            }

        }

    }

    private fun bindViewHolder(holder: BooksBlockViewHolder, position: Int) {
        holder.render(data[position], position)
    }
}

/**
 * @param itemClickListener Int: ui的position，因为切换之后会变  Int: data的真实position
 */
class BooksBlockViewHolder(itemView: View, val maxSize:Int, val itemClickListener: (Int, Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val tvName: TextView by lazy {
        itemView.findViewById(R.id.tvName)
    }

    private val leftArc: ArcLeftView by lazy {
        itemView.findViewById(R.id.leftArc)
    }

    private val rightArc: RightArcView by lazy {
        itemView.findViewById(R.id.rightArc)
    }

    private val tvCreate : TextView by lazy {
        itemView.findViewById(R.id.tvCreate)
    }

    private val receive:TextView by lazy {
        itemView.findViewById(R.id.receive)
    }

    private val spend:TextView by lazy {
        itemView.findViewById(R.id.spend)
    }

    private val diff: TextView by lazy {
        itemView.findViewById(R.id.diff)
    }

    private var currentMarginTop = 0
    private var selfPosition = -1
        set(value) {
            field = value
            rightArc.isVisible = value == maxSize - 1
        }

    val uiPosition: Int
        get() = selfPosition
    private var selfData: BookUI? = null

    private val singleMarginTop by lazy { dp2px(itemView.context, 47f) }

    fun refreshData(bookUi: BookUI) {
        val book = bookUi.book
        receive.text = book.income.toString()
        spend.text = String.format(getString(R.string.spend_content), book.spend.toString())
        diff.text =( book.income - book.spend).toString()
    }

    fun render(bookUi: BookUI, position: Int) {
        val book = bookUi.book
        val context = itemView.context
        selfPosition = position
        selfData = bookUi
        itemView.layoutParams = (itemView.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
            startToStart = 0
            endToEnd = 0
            topToTop = 0
            currentMarginTop = singleMarginTop * position
            topMargin = currentMarginTop
        }
        itemView.background = configDrawable(context) {
            radius = 8f
            gradientColors = intArrayOf(
                ContextCompat.getColor(context, bookUi.background.startColor),
                ContextCompat.getColor(context, bookUi.background.endColor)
            )
            solid = Color.WHITE
        }
        leftArc.render(
            Size(dp2px(context, 139f), dp2px(context, 105f)),
            ContextCompat.getColor(context, bookUi.background.arcColor)
        )
        rightArc.render(
            Size(dp2px(context, 128f), dp2px(context, 149f)),
            ContextCompat.getColor(context, bookUi.background.arcColor)
        )
        itemView.setOnClickListener {
            itemClickListener.invoke(selfPosition, position)
        }

        tvName.text = book.name
        tvName.background = configDrawable(context){
            solid = ContextCompat.getColor(context, R.color.black_24)
            radius = 56f
        }

        tvCreate.background = configDrawable(context) {
            solid = ContextCompat.getColor(context,R.color.white)
            radius = 24f
        }

        tvCreate.setOnClickListener {
            (tvCreate.context as? FragmentActivity)?.let {owner->
                val viewModel = ViewModelProvider(owner)[BooksViewModel::class.java]
                RecordCreateActivity.launchCreate(owner, viewModel.books.value, currentBook = bookUi.book)
            }
        }

        receive.text = book.income.toString()
        spend.text = String.format(getString(R.string.spend_content), book.spend.toString())
        diff.text =( book.income - book.spend).toString()
    }

    fun toCollect(frontPosition: Int, finish: () -> Unit, reSort: (Int) -> Unit) {
        if (frontPosition == selfPosition) {
            (itemView.parent as? ConstraintLayout)?.apply {
                indexOfChild(itemView).takeIf {
                    it > -1
                }?.let { index ->
                    rightArc.isVisible = true
                    removeViewAt(index)
                    addView(itemView)
                }
            }
        }
        ValueAnimator.ofInt(currentMarginTop, 0).apply {
            duration = 1000
            addUpdateListener { animator ->
                val marginTop = animator.animatedValue as Int
                itemView.layoutParams =
                    (itemView.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
                        topMargin = marginTop
                    }
            }
            doOnEnd {
                finish()
                if (frontPosition == selfPosition) {
                    reSort.invoke(selfPosition)
                }
            }
            start()
        }
    }

    fun toExpand() : ValueAnimator{
        val marginTop = selfPosition * singleMarginTop
        return ValueAnimator.ofInt(0, marginTop).apply {
            duration = 1000
            addUpdateListener { animator ->
                itemView.layoutParams =
                    (itemView.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
                        topMargin = animator.animatedValue as Int
                    }
            }
            doOnEnd {
                currentMarginTop = marginTop

            }
            start()
        }
    }

    fun back(target: Int) {
        selfPosition = target
    }

    fun font() {
        selfPosition -= 1
    }
}

enum class BooksBlockStatus {
    Expand, // 展开
    Collect //收起
}