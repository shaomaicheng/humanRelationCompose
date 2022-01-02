package com.chenglei.humanrelationbooking.books

import com.chenglei.humanrelationbooking.applog
import com.chenglei.humanrelationbooking.base.ListMeta
import com.chenglei.humanrelationbooking.base.ListViewModel
import com.chenglei.humanrelationbooking.base.PageData
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.meta.BookItem

class BookRecordListViewModel : ListViewModel<Book, BookItem>() {
    override suspend fun fetch(pageData: PageData<Book>): ListMeta<BookItem> {
        applog("首页数据拉取，第${pageData.pageNo}页, limit:${pageData.pageSize}, skip:${(pageData.pageNo) * pageData.pageSize}")
//        val query = BmobQuery<BookItem>()
//        query.addWhereEqualTo("userId", BmobUser.getCurrentUser().objectId)
//            .addWhereEqualTo("bookName", pageData.param?.name)
//            .addWhereEqualTo("type", pageData.param?.queryType)
//            .order("-time")
//            .setLimit(pageData.pageSize)
//            .setSkip((pageData.pageNo) * pageData.pageSize)
//        val bookItems = query.findObjectsSync(BookItem::class.java) ?: throw BmobNetworkException()
//        applog("数据")
//        bookItems.forEach {
//            applog(it.username)
//        }
//        return ListMeta(bookItems, bookItems.size == pageData.pageSize)
        return ListMeta(emptyList(),false)
    }
}