package com.chenglei.humanrelationbooking.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object EventCenter {

    private val channels = mutableMapOf<String, MutableStateFlow<out Any?>>()

    fun<T:Any?> getChannel(channel:String): MutableStateFlow<out Any?>? {
        if (channels[channel] != null) return channels[channel]
        val flow = MutableStateFlow<T?>(null)
        channels[channel] = flow
        return flow
    }

    fun <T:Any> post(channel: String, value:T) {
        (getChannel<T>(channel = channel) as? MutableStateFlow<T>)?.value = value
    }

    fun<T:Any> observer(channel: String, scope:CoroutineScope, observer:(T?)->Unit) {
        scope.launch {
            (getChannel<T?>(channel = channel) as? MutableStateFlow<T?>)?.collectLatest {
                observer.invoke(it)
            }
        }
    }
}


class EventCenterKey {
    companion object {
        const val Book = "book"
        const val Relationitem = "relationItem"
        const val BookItem = "bookItem"
        const val RelationRefresh = "relation_refresh"
    }
}