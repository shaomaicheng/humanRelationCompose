package com.chenglei.humanrelationbooking.meta

import androidx.annotation.Keep
import cn.bmob.v3.BmobObject
import com.chenglei.humanrelationbooking.books.create.BookItemType


@Keep
data class Book(
    val name:String,
    val userId:String,
    var income:Int,
    var spend:Int
): BmobObject() {
    @Transient var queryType : Int = BookItemType.Income.type
}