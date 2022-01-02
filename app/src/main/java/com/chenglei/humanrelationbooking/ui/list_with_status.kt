package com.chenglei.humanrelationbooking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.ListViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun <P, T : Any, VM : ListViewModel<P, T>> ListWithStatus(
    vm: VM, listModifier: Modifier = Modifier,
    keyGenerator: (T) -> String,
    load:()->Unit,
    empty:@Composable ()->Unit,
    itemComposable : @Composable (Int, T)->Unit

) {
    val context = LocalContext.current
    val lazyPagingItems = vm.listFlow.collectAsLazyPagingItems()
    val isLoading = vm.loading.collectAsState()
    val initPageCompleted = vm.initPageCompleted.collectAsState()
    val refreshState =
        rememberSwipeRefreshState(isRefreshing = isLoading.value)

    DisposableEffect("") {
        load()
        val dispose = onDispose {
            vm.initPageCompleted.value = false
            vm.loading.value = false
        }
        dispose
    }

    SwipeRefresh(state = refreshState, onRefresh = {
        vm.refresh()
    }, indicator = { state, refreshTrigger ->
        SwipeRefreshIndicator(
            state = state, refreshTriggerDistance = refreshTrigger,
            backgroundColor = colorResource(id = R.color.white),
            contentColor = colorResource(id = R.color.primary_500)
        )
    }) {
        if (initPageCompleted.value && lazyPagingItems.loadState.refresh is LoadState.NotLoading) {

            if (lazyPagingItems.itemCount == 0) {
                // 空视图
                empty()
            } else {
                LazyColumn(
                    modifier = listModifier
                ) {

                    itemsIndexed(
                        lazyPagingItems,
                        key = { index, item -> keyGenerator.invoke(item) }) { index, item ->
                        item?.let {
                            itemComposable(index, item)
                        } ?: Empty()
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Loading(loading = true, Modifier.align(Alignment.Center))
            }
        }
    }
}