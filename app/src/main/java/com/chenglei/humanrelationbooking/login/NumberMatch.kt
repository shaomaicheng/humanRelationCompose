package com.chenglei.humanrelationbooking.login

interface NumberMatch {
    fun match(phone:String): Boolean
}


class PhoneNumberMatch: NumberMatch {
    override fun match(phone: String): Boolean {
        return phone.length == 11
    }
}


class SmsCodeMatch:NumberMatch {
    override fun match(smsCode: String): Boolean {
        return smsCode.length == 6
    }

}