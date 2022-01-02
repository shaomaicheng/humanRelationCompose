package com.chenglei.humanrelationbooking.profile

import cn.bmob.v3.BmobUser

data class UserBase(
    val avatarUrl:String? = "",
    val gender:Int = Gender.Unknow.value,
    val birthday:Long = 0L
): BmobUser() {

}

enum class Gender(val value:Int){
    Male(1),
    Female(2),
    Unknow(3)
}