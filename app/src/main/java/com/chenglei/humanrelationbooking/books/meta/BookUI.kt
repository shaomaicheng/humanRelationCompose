package com.chenglei.humanrelationbooking.books.meta

import com.chenglei.humanrelationbooking.books.BookBackground
import com.chenglei.humanrelationbooking.meta.Book

data class BookUI(
    var book:Book,
    val background:BookBackground
)