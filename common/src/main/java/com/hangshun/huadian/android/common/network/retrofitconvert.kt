package com.hangshun.huadian.android.common.network

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class MineConvertFactory: Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (getRawType(type) != ApiResult::class.java) {
            return null
        }
        val rawType = (type as ParameterizedType).actualTypeArguments[0]
        val adapter = Gson().getAdapter(TypeToken.get(rawType))
        return MineConvert(adapter as TypeAdapter<Any>)
    }
}

class MineConvert<T>(val adapter:TypeAdapter<T>) : Converter<ResponseBody, ApiResult<T>> {
    override fun convert(value: ResponseBody): ApiResult<T>? {
        val response = value.string()
        return try {
           ApiResult.success( adapter.fromJson(response))
//            ApiResult.success(Gson().fromJson<T>(response, object : TypeToken<T>(){}.type))
        } catch (e:Exception){
            e.printStackTrace()
            ApiResult(response, null, 200) as? ApiResult<T>
        }
    }
}