package com.chenglei.humanrelationbooking

import android.app.Application
import android.content.Context

class BooksApp :Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


    companion object {

        private lateinit var context: Context

        @JvmStatic
        fun getContext():Context {
            return context
        }
    }
}
