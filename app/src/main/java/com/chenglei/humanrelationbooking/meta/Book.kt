package com.chenglei.humanrelationbooking.meta

import androidx.annotation.Keep
import com.chenglei.humanrelationbooking.books.create.BookItemType


@Keep
data class Book(
    val name:String,
    var income:Int,
    var spend:Int
) {
    @Transient var queryType : Int = BookItemType.Income.type
}