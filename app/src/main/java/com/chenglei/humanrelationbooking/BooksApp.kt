package com.chenglei.humanrelationbooking

import android.app.Application
import android.content.Context
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser

class BooksApp :Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Bmob.initialize(this,"fd69ecc3017a9d9f558666387ac3f446")
    }


    companion object {

        private lateinit var context: Context

        @JvmStatic
        fun getContext():Context {
            return context
        }

        @JvmStatic
        fun getDataBaseName():String{
            return "humanrelation-${BmobUser.getCurrentUser().objectId}"
        }
    }
}
