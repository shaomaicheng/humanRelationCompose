package com.chenglei.humanrelationbooking.base.arch

import com.hangshun.huadian.android.common.meta.CommonStatus
import com.hangshun.huadian.android.common.meta.ParamAndResult
import com.hangshun.huadian.android.common.network.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class FlowCollectDataSource<T>(private val scope:CoroutineScope) {
    private val _flow : MutableStateFlow<T?> = MutableStateFlow(null)
    private val flow: Flow<T?> = _flow.flatMapLatest { flow { emit(it) } }

    fun request(request: (()->Flow<T>)) {
        scope.launch(Dispatchers.IO) {
            request.invoke().collect {
                _flow.value = it
            }
        }
    }

    fun observer(observer: (T?)->Unit) {
        scope.launch {
            flow.collectLatest {
                observer.invoke(it)
            }
        }
    }
}


fun <P,R> newFetch(param: P, remote: suspend (P) -> ApiResult<R>): Flow<ParamAndResult<P, ApiResult<R>>> =
    flow {
        emit(ParamAndResult.loading(param))
        try {
            val result = apiResultToFlow(remote, param)
            emit(result)
        }catch (e: Exception){
            e.printStackTrace()
            emit(ParamAndResult.error<P, ApiResult<R>>(param, e))
        }
    }


suspend fun<P,R> apiResultToFlow(remote: suspend (P) -> ApiResult<R>, param: P): ParamAndResult<P, ApiResult<R>> {
    return ParamAndResult(param, remote(param),null, CommonStatus.Success)
}



fun<P,T> FlowCollectDataSource<ParamAndResult<P,T>>.use(
    success: ((P,T)->Unit)? = null,
    loading:(()->Unit)? = null,
    error:((String)->Unit)? = null
) {
    observer {
        it?.let {
            when (it.status) {
                CommonStatus.Loading-> loading?.invoke()
                CommonStatus.Success -> {
                    it.param?.let { p->
                        it.result?.let { ret->
                            success?.invoke(p, ret)
                        }
                    }
                }
                CommonStatus.Error-> it.error?.toString()?.let { it1 -> error?.invoke(it1) }
            }
        }
    }
}