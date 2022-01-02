package com.hangshun.huadian.android.common.arch

import androidx.lifecycle.*
import com.hangshun.huadian.android.common.meta.CommonStatus
import com.hangshun.huadian.android.common.meta.ParamAndResult
import com.hangshun.huadian.android.common.network.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class BaseViewModel:ViewModel(){

}

fun ViewModel.postToIO(runnable: ()->Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        runnable()
    }
}

fun ViewModel.postToMain(runnable: ()->Unit) {
    viewModelScope.launch(Dispatchers.Main) {
        runnable()
    }
}

open class BaseRepo {
}

open class ObservableDataSource<T>(scope: CoroutineScope) {
    private val mediator = MediatorLiveData<T>()
    private var last: LiveData<T>? = null

    fun request(request:(()->LiveData<T>)) {
        last?.let {
            mediator.removeSource(it)
        }

        last = request.invoke()
        last?.let {
            mediator.addSource(it) {data->
                mediator.value = data
            }
        }
    }

    fun observer(owner: LifecycleOwner, observer: Observer<T>) {
        mediator.observe(owner,observer)
    }

    fun removeObserver(observer: Observer<T>){
        mediator.removeObserver(observer)
    }

    fun observeForever(observer: Observer<T>) {
        mediator.observeForever(observer)
    }
}

fun <P,R> fetch(param: P, remote: suspend (P) -> ApiResult<R>): LiveData<ParamAndResult<P,ApiResult<R>>> =
    liveData(Dispatchers.IO) {
        emit(ParamAndResult.loading<P,ApiResult<R>>(param))
        try {
            val result = apiResultToLiveData(remote, param)
            emit(result)
        }catch (e: Exception){
            e.printStackTrace()
            emit(ParamAndResult.error<P, ApiResult<R>>(param, e))
        }
    }

suspend fun<P,R> apiResultToLiveData(remote: suspend (P) -> ApiResult<R>, param: P):ParamAndResult<P,ApiResult<R>> {
    return ParamAndResult(param, remote(param),null, CommonStatus.Success)
}