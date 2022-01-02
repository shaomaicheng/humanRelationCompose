package com.hangshun.huadian.android.common.meta

import java.lang.Exception

class ParamAndResult<P,R>(
    val param:P?,
    val result:R?,
    val error: Exception?=null,
    val status : CommonStatus
) {
    companion object{
        @JvmStatic
        fun<P,R> loading(param: P): ParamAndResult<P,R>{
            return ParamAndResult(
                param = param,
                result = null,
                error = null,
                status = CommonStatus.Loading
            )
        }

        @JvmStatic
        fun<P,R> error(param:P, e:Exception): ParamAndResult<P,R> {
            return ParamAndResult(
                param = param,
                result = null,
                error = e,
                status = CommonStatus.Error
            )
        }
    }
}

enum class CommonStatus {
    Loading,
    Success,
    Error
}