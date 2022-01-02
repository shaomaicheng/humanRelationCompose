package com.chenglei.humanrelationbooking.vms

import com.hangshun.huadian.android.common.arch.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RelationDetailViewModel:BaseViewModel() {

    val type = MutableStateFlow(Type.All)

    enum class Type(val index:Int) {
        All(0), Income(1), Spend(2);

        companion object {
            fun of(index:Int) : Type{
                return when (index) {
                    0->All
                    1->Income
                    else->Spend
                }
            }
        }
    }
}