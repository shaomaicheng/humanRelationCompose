package com.chenglei.humanrelationbooking.vms

import androidx.lifecycle.viewModelScope
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.arch.FlowCollectDataSource
import com.chenglei.humanrelationbooking.utils.toast
import com.hangshun.huadian.android.common.arch.BaseViewModel
import com.hangshun.huadian.android.common.meta.ParamAndResult
import com.hangshun.huadian.android.common.network.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class RelationEditVM : BaseViewModel() {

    val editDs by lazy {
        RelationEditDataSource(viewModelScope)
    }

    val name = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val remark = MutableStateFlow("")
    val relation = MutableStateFlow("")


    fun save() {
        if (name.value.isEmpty()) {
            toast(R.string.toast_relation_name)
            return
        }
        if (relation.value.isEmpty()) {
            toast(R.string.toast_relation_relation)
            return
        }
        editDs.save(name.value, phone.value, remark.value, relation.value)
    }
}


class RelationEditDataSource(scope: CoroutineScope) :
    FlowCollectDataSource<ParamAndResult<RelationItem, ApiResult<Any>>>(scope) {

    fun save(
        name: String,
        phone: String,
        remark: String,
        relation: String
    ) {


    }
}