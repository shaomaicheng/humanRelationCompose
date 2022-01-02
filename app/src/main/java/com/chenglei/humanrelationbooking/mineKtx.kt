package com.chenglei.humanrelationbooking

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <reified VM:ViewModel> ViewModelStoreOwner.requireViewModel()
    = ViewModelProvider(this)[VM::class.java]


inline fun <reified T> LifecycleOwner.flowOb(flow: Flow<T>, crossinline action: suspend (value: T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(action = action)
        }
    }
}


inline fun <reified T> LifecycleOwner.flowObLast(flow: Flow<T>, crossinline action: suspend (value: T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                action(it)
            }
        }
    }
}


fun ViewModel.io( block:suspend ()->Unit) {
    viewModelScope.launch(IO) {
        block()
    }
}

fun Context.version():String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}