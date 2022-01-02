package com.chenglei.humanrelationbooking.utils

import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import cn.bmob.v3.BmobQuery
import com.chenglei.humanrelationbooking.BooksApp
import com.hangshun.huadian.android.common.meta.CommonStatus
import com.hangshun.huadian.android.common.meta.ParamAndResult
import com.hangshun.huadian.android.common.network.ApiResult
import java.lang.Exception


fun<P,R> ParamAndResult<P,R>.isSuccess() = this.status == CommonStatus.Success
fun<P,R> ParamAndResult<P,R>.isLoading() = this.status == CommonStatus.Loading
fun<P,R> ParamAndResult<P,R>.isError() = this.status == CommonStatus.Error

fun<P,T> ParamAndResult<P,ApiResult<T>>.useSuccess(block:(P,T)->Unit){
    this.takeIf { isSuccess() }?.apply {
        this.result?.let { result->
            this.param?.let { param->
                result.data?.let { data->

                    block.invoke(param, data)
                }

            }
        }
    }
}


fun<P,T> ParamAndResult<P,ApiResult<T>>.use(success:(P,T)->Unit,error:(Exception)->Unit, loading:(()->Unit)? = null){
    when {
        isSuccess() -> {
            useSuccess(success)
        }
        isError() -> {
            this.error?.apply {
                error(this)
            }
        }
        else -> {
            loading?.invoke()
        }
    }
}



fun<E> MutableList<E>.replace(new:List<E>) {
    this.clear()
    this.addAll(new)
}


fun<T> BmobQuery<T>.useQuery(block: (BmobQuery<T>) -> List<T>):List<T> {
    return try {
        block(this)
    } catch (e:Exception) {
        e.printStackTrace()
        emptyList()
    }
}

fun getString(@StringRes res: Int) = BooksApp.getContext().getString(res)
fun getCompatColor(@ColorRes colorRes:Int) = ContextCompat.getColor(BooksApp.getContext(), colorRes)
fun getCompatDrawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(BooksApp.getContext(), drawableRes)

fun toast(@StringRes res:Int) = toast(getString(res))
fun toast(content:String){
    Toast.makeText(BooksApp.getContext(), content, Toast.LENGTH_SHORT).show()
}