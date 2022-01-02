package com.chenglei.humanrelationbooking

import androidx.annotation.StringRes

fun getString(@StringRes id : Int) = BooksApp.getContext().getString(id)