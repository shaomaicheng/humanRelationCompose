package com.chenglei.humanrelationbooking.vms

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import com.chenglei.humanrelationbooking.applog
import com.chenglei.humanrelationbooking.base.ListMeta
import com.chenglei.humanrelationbooking.base.ListViewModel
import com.chenglei.humanrelationbooking.base.PageData
import kotlinx.coroutines.flow.MutableStateFlow

class RelationListViewModel:ListViewModel<String, RelationItem>() {
    override suspend fun fetch(pageData: PageData<String>): ListMeta<RelationItem> {
        if (pageData.param?.isEmpty() == true) {
            applog("全部联系人，第${pageData.pageNo}页")
        }
        val query = BmobQuery<RelationItem>()
            .addWhereEqualTo("owner", BmobUser.getCurrentUser().objectId)
            .order("-createdAt")
            .setLimit(pageData.pageSize)
            .setSkip(pageData.pageNo * pageData.pageSize)
        if (pageData.param?.isNotEmpty() == true) {
            query.addWhereEqualTo("relation", pageData.param)
        }
        val items = query.findObjectsSync(RelationItem::class.java)
        return ListMeta(items, items.size == pageData.pageSize)
    }
}