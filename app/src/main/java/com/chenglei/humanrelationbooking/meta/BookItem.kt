package com.chenglei.humanrelationbooking.meta

import androidx.annotation.Keep

@Keep
data class BookItem(
    val type:Int,
    val money:Int,
    val bookName:String,
    val relation: String,
    val timestamp: Long,
    val username:String,
    val time:Long = System.currentTimeMillis()
) {
    companion object {
        @JvmStatic
        val Type_Income = 1

        @JvmStatic
        val Type_Spend = 2
    }
}