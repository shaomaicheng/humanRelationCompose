package com.chenglei.humanrelationbooking.books.datasource

import android.content.Context
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.SaveListener
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.arch.FlowCollectDataSource
import com.chenglei.humanrelationbooking.base.arch.newFetch
import com.chenglei.humanrelationbooking.meta.Book
import com.hangshun.huadian.android.common.meta.ParamAndResult
import com.hangshun.huadian.android.common.network.ApiResult
import kotlinx.coroutines.CoroutineScope


/**
 * 账本数据源
 * 本地数据库拉取
 * 如果数据库不存在，系统预置+服务端拉取后存在本地
 */

class NewBooksDataSource(val context: Context, private val scope: CoroutineScope) :
    FlowCollectDataSource<ParamAndResult<String, ApiResult<List<Book>>>>(scope) {

    private val systems = listOf(
        context.getString(R.string.books_type_newstore),
        context.getString(R.string.books_type_study),
        context.getString(R.string.books_type_birthday),
        context.getString(R.string.books_type_wedding),
        context.getString(R.string.books_type_baby),
        context.getString(R.string.books_type_newYearKids),
        context.getString(R.string.books_type_baby_fullMoon),
        context.getString(R.string.books_type_baby_age),
        context.getString(R.string.books_type_funeral)
    )


    fun getBooks() {
        request {
            newFetch("") {
                val mine = BmobUser.getCurrentUser().objectId
                val remotes= BmobQuery<Book>().addWhereEqualTo("userId", mine)
                    .findObjectsSync(Book::class.java)

                if (remotes.isNotEmpty()) {
                    ApiResult.success(remotes)
                } else {

                    val finalRet = systems.map {
                        Book(it, BmobUser.getCurrentUser().objectId, 0, 0)
                    }
                    finalRet.forEach {book->
                        if (BmobQuery<Book>()
                                .addWhereEqualTo("userId", BmobUser.getCurrentUser().objectId)
                                .addWhereEqualTo("name", book.name)
                                .findObjectsSync(Book::class.java).isEmpty()) {
                            book.save(object : SaveListener<String>() {
                                override fun done(p0: String?, p1: BmobException?) {}
                            })
                        }
                    }
                    ApiResult.success(finalRet)
                }
            }
        }
    }

}