package com.hangshun.huadian.android.common.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class MineApiAdapterFactory : CallAdapter.Factory(){
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != ApiResult::class.java){
            return null
        }
        if (rawType !is ParameterizedType){
            throw  IllegalAccessException("ApiResult必须制定泛型")
        }
        return MineApiAdapter<Any>(rawType)
    }
}

class MineApiAdapter<T>(private val rawType: Type) : CallAdapter<T, ApiResult<T>> {
    override fun adapt(call: Call<T>): ApiResult<T> {
        val response = call.execute()
        return if (!response.isSuccessful || response.body() == null){
            ApiResult(null, response.errorBody()?.string(),response.code())
        } else{
            val t = response.body()
            ApiResult(t, null, response.code())
        }
    }

    override fun responseType(): Type {
        return rawType
    }

}