package com.chenglei.humanrelationbooking.profile


data class UserBase(
    val avatarUrl:String? = "",
    val gender:Int = Gender.Unknow.value,
    val birthday:Long = 0L
) {

}

enum class Gender(val value:Int){
    Male(1),
    Female(2),
    Unknow(3)
}