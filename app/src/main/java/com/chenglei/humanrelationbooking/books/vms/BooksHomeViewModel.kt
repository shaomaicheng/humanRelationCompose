package com.chenglei.humanrelationbooking.books.vms

import com.chenglei.humanrelationbooking.books.BooksBlockStatus
import com.chenglei.humanrelationbooking.books.create.BookItemType
import com.chenglei.humanrelationbooking.meta.Book
import com.hangshun.huadian.android.common.arch.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class BooksHomeViewModel:BaseViewModel() {


    val type = MutableStateFlow(BookItemType.Income)
    val status = MutableStateFlow(BooksBlockStatus.Expand)

    val currentBook = MutableStateFlow<Book?>(null)

    val expend = MutableStateFlow(true)
}