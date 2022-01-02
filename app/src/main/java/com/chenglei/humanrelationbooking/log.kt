package com.chenglei.humanrelationbooking

import android.util.Log

fun applog(log:String) {
    if (BuildConfig.DEBUG) {
        Log.e("chenglei", log)
    }
}