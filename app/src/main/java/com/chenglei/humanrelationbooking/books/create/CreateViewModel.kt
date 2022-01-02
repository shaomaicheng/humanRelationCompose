package com.chenglei.humanrelationbooking.books.create

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.EventCenter
import com.chenglei.humanrelationbooking.base.EventCenterKey
import com.chenglei.humanrelationbooking.io
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.meta.BookItem
import com.chenglei.humanrelationbooking.utils.getString
import com.chenglei.humanrelationbooking.utils.toast
import com.chenglei.humanrelationbooking.vms.RelationItem
import com.chenglei.humanrelationbooking.vms.income
import com.chenglei.humanrelationbooking.vms.new
import com.chenglei.humanrelationbooking.vms.spend
import com.hangshun.huadian.android.common.arch.BaseViewModel
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

@InternalCoroutinesApi
class CreateViewModel : BaseViewModel() {

    private val _book: MutableStateFlow<Book?> = MutableStateFlow(null)
    val book: StateFlow<Book?>
        get() = _book


    private val _username: MutableStateFlow<String> = MutableStateFlow("")
    val username: StateFlow<String>
        get() = _username

    private val _time: MutableStateFlow<Long> = MutableStateFlow(0L)
    val time: StateFlow<Long>
        get() = _time

    private val _relation: MutableStateFlow<String> = MutableStateFlow("")
    val relation: StateFlow<String>
        get() = _relation

    private val _type = MutableStateFlow<BookItemType>(BookItemType.Income)
    val type: StateFlow<BookItemType>
        get() = _type

    private val _money = MutableStateFlow(0)
    val money: StateFlow<Int>
        get() = _money


    private val _submitSuccess = MutableStateFlow(false)
    val submitSuccess : StateFlow<Boolean>
        get() = _submitSuccess


    fun initData(bookItem: BookItem) {
        io {
//            BmobQuery<Book>().addWhereEqualTo("userId", BmobUser.getCurrentUser().objectId)
//                .addWhereEqualTo("name", bookItem.bookName)
//                .findObjects(object : FindListener<Book>() {
//                    override fun done(p0: MutableList<Book>?, p1: BmobException?) {
//                        p0?.let { books->
//                            books.takeIf { it.size == 1 }?.let { this@CreateViewModel._book.value = it[0] }
//                        }
//                    }
//
//                })
        }
        _username.value = bookItem.username
        _time.value = bookItem.timestamp
        _type.value= if (bookItem.type == BookItemType.Income.type) BookItemType.Income else BookItemType.Spend
        _relation.value = bookItem.relation
        _money.value = bookItem.money
    }

    fun chooseBook(book: Book) {
        _book.value = book
    }

    fun inputName(username: String) {
        _username.value = username
    }


    fun chooseRelation(relation: String) {
        _relation.value = relation
    }

    fun switchType(newType: BookItemType) {
        _type.value = newType
    }

    fun inputMoney(money: Int) {
        this._money.value = money
    }


    fun updateRelationItem(relationItem: RelationItem) {
        this._username.value = relationItem.name
        this._relation.value = relationItem.relation
    }

    fun updateTime(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        _time.value = calendar.timeInMillis
    }


    fun canSave(): Boolean {
        return money.value != 0 &&
                book.value != null &&
                username.value.isNotEmpty() &&
                relation.value.isNotEmpty() &&
                time.value != 0L
    }

    private fun emptyJudge():Boolean {
        if (money.value == 0) {
            toast(R.string.toast_money)
            return true
        }

        if (book.value == null) {
            toast(R.string.toast_books)
            return true
        }

        if (this._username.value.isEmpty()) {
            toast(R.string.toast_name)
            return true
        }

        if (this._relation.value.isEmpty()) {
            toast(R.string.toast_relation)
            return true
        }

        if (this._time.value == 0L) {
            toast(R.string.toast_date)
            return true
        }
        return false
    }

    fun editRecord(oldItem: BookItem) {
        if (emptyJudge()) {
            return
        }

        val bookItem = BookItem(
            username = username.value,
            type = type.value.type,
            money = money.value,
            bookName = book.value!!.name,
            relation = relation.value,
            timestamp = time.value,
        )

        io {
//            val books = BmobQuery<Book>()
//                .addWhereEqualTo("userId", BmobUser.getCurrentUser().objectId)
//                .addWhereEqualTo("name", bookItem.bookName)
//                .findObjectsSync(Book::class.java)
//            val relations = BmobQuery<RelationItem>()
//                .addWhereEqualTo("name", bookItem.username)
//                .addWhereEqualTo("relation", bookItem.relation)
//                .addWhereEqualTo("owner", BmobUser.getCurrentUser().objectId)
//                .findObjectsSync(RelationItem::class.java)

            val books = emptyList<Book>()
            val relations = emptyList<RelationItem>()
            books.takeIf {
                it.size == 1
            }?.let { books->
                val book = books[0]
                if (bookItem.type == oldItem.type) {
                    if (bookItem.type == BookItemType.Income.type) {
                        book.income -= (bookItem.money - oldItem.money)
                    } else {
                        book.spend -= (bookItem.money - oldItem.money)
                    }
                } else {
                    if (bookItem.type == BookItemType.Income.type) {
                        book.income += bookItem.money
                        book.spend -= oldItem.money
                    } else {
                        book.spend += bookItem.money
                        book.income -= oldItem.money
                    }
                }

                var relationItemUpdate : RelationItem? = null
                relations.takeIf {
                    it.size == 1
                }?.let { relations->
                    val relationItem = relations[0]
                    if (bookItem.type == oldItem.type) {
                        if (bookItem.type == BookItemType.Income.type) {
                            relationItem.income -= (bookItem.money - oldItem.money)
                        } else {
                            relationItem.spend -= (bookItem.money - oldItem.money)
                        }
                    } else {
                    if (bookItem.type == BookItemType.Income.type) {
                        relationItem.income += bookItem.money
                        relationItem.spend -= oldItem.money
                    } else {
                        relationItem.spend += bookItem.money
                        relationItem.income -= oldItem.money
                    }
                }
                    relationItemUpdate = relationItem.new()
                }
                val updateBook = Book(book.name,book.income, book.spend)

//                BmobBatch()
//                    .updateBatch(
//                        relationItemUpdate?.let {relationItemUpdate->
//                            listOf(
//                                bookItem,
//                                updateBook.apply { objectId = book.objectId },
//                                relationItemUpdate
//                            )
//                        }?: listOf(bookItem, updateBook.apply { objectId = book.objectId })
//                    )
//                    .doBatch(object : QueryListListener<BatchResult>() {
//                        override fun done(result: MutableList<BatchResult>?, e: BmobException?) {
//                            e?.let {
//                                toast(R.string.toast_save_fail)
//                            } ?: kotlin.run {
//                                _submitSuccess.value = true
//                                EventCenter.post(EventCenterKey.Book, updateBook)
//                                relationItemUpdate?.let { relationItemUpdate->
//                                    EventCenter.post(EventCenterKey.Relationitem, relationItemUpdate)
//                                }
//                                result?.let {
//                                    it[0]?.let { result->
//                                        bookItem.objectId = result.objectId
//                                        EventCenter.post(EventCenterKey.BookItem, bookItem)
//                                    }
//                                }
//                            }
//                        }
//
//                    })
            } ?: kotlin.run {
                Handler(Looper.getMainLooper()).run {
                    toast(R.string.toast_save_fail)
                }
            }
        }
    }


    @InternalCoroutinesApi
    fun saveRecord() {


        if (emptyJudge()) {
            return
        }

        val bookItem = BookItem(
            username = username.value,
            type = type.value.type,
            money = money.value,
            bookName = book.value!!.name,
            relation = relation.value,
            timestamp = time.value,
        )
    }

}


@BindingAdapter("showTimeFormat")
fun showTimeFormat(textView: TextView, time: Long) {
    textView.text = if (time == 0L) textView.context.getString(R.string.book_time_title_tips)
    else {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val dateFormat = SimpleDateFormat(textView.context.getString(R.string.date_format))
        dateFormat.format(calendar.time)

    }
}

