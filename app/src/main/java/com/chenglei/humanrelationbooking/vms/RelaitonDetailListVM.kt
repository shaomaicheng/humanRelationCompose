package com.chenglei.humanrelationbooking.vms

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.base.ListMeta
import com.chenglei.humanrelationbooking.base.ListViewModel
import com.chenglei.humanrelationbooking.base.PageData
import com.chenglei.humanrelationbooking.meta.BookItem

class RelaitonDetailListVM : ListViewModel<RelationBooksRequest, BookItem>() {
    override suspend fun fetch(pageData: PageData<RelationBooksRequest>): ListMeta<BookItem> {
        val query = BmobQuery<BookItem>()
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser().objectId)
        query.addWhereEqualTo("username", pageData.param?.name?:"")
        if (pageData.param?.type != 0) {
            query.addWhereEqualTo("type", pageData.param?.type)
        }
        query.order("-timestamp")
        query.setLimit(pageData.pageSize)
        query.setSkip(pageData.pageNo * pageData.pageSize)
        val items = query.findObjectsSync(BookItem::class.java)
        return ListMeta(items, items.size == pageData.pageSize)
    }
}


data class RelationBooksRequest(
    val name:String,
    val type:Int
)