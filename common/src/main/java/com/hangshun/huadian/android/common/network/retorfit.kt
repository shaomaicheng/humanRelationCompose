package com.hangshun.huadian.android.common.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


fun createRetrofit():Retrofit{
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    return Retrofit.Builder()
        .baseUrl("http://blockchain.tunnel.shunjiantech.cn/")
        .addConverterFactory(MineConvertFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(MineApiAdapterFactory())
        .client(client)
        .build()
}

inline fun < reified SERVICE> Retrofit.serviceCreate():SERVICE {
    return create(SERVICE::class.java)
}

class ApiResult<T>(
    val data:T?,
    val error:String?,
    val code:Int
) {
    companion object{
        @JvmStatic
        fun<T> fail(error: String?):ApiResult<T>{
            return ApiResult(null, error, 500)
        }

        @JvmStatic
        fun<T> success(data: T):ApiResult<T> {
            return ApiResult(data, null, 200)
        }
    }
}