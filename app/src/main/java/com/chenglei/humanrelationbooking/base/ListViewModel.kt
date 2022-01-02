package com.chenglei.humanrelationbooking.base

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hangshun.huadian.android.common.arch.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

abstract class ListViewModel<P, T : Any> : BaseViewModel(), IFetcher<P, T> {

    private var param : P? = null
    val p: PageData<P>?
        get() = pageData
    private var pageData: PageData<P>? = null

    companion object {
        const val PAGE_SIZE = 10
    }

    private var source: MinePagingSource<P, T>? = null

    private fun initPageDataIfNeed() {
        if (pageData == null) {
            pageData = PageData(0, PAGE_SIZE, param)
        }
    }

    val loading = MutableStateFlow(false)
    val initPageCompleted = MutableStateFlow(false)

    private fun resetPageData() {
        pageData?.let { pageData ->
            pageData.hasMore = true
            pageData.pageNo = 0
            pageData.param = param
        }
    }

    val listFlow: Flow<PagingData<T>> = Pager(
        PagingConfig(PAGE_SIZE, enablePlaceholders = true, prefetchDistance = PAGE_SIZE, initialLoadSize = PAGE_SIZE)
    ) {
        initPageDataIfNeed()
        val pagingSource = MinePagingSource(pageData, this, {isLoading->
            loading.value = isLoading
        }, {initCompleted->
            initPageCompleted.value = initCompleted
        })
        this@ListViewModel.source?.apply { pagingSource.copy(this) }
        this@ListViewModel.source = pagingSource
        pagingSource
    }.flow.cachedIn(viewModelScope)

    fun load(p: P) {
        param = p
        resetPageData()
        source?.invalidate()
    }

    fun refresh() {
        initPageCompleted.value = false
        loading.value = false
        resetPageData()
        source?.invalidate()
    }

    fun listOperator(): IOperator<P, T>? {
        return source
    }

}

data class ListMeta<T>(
    val pageList: List<T>,
    val hasMore: Boolean
)

interface IFetcher<P, T> {
    suspend fun fetch(pageData: PageData<P>): ListMeta<T>
}

interface IOperator<P, T> {
    fun get(): List<T>
}

data class PageData<P>(
    var pageNo: Int,
    var pageSize: Int,
    var param: P?,
    var hasMore: Boolean = true
)


class MinePagingSource<P, T : Any>(
    private val pageData: PageData<P>?,
    private val fetcher: IFetcher<P, T>,
    private val loading:(Boolean)->Unit,
    private val initLoadComplete:(Boolean)->Unit
) : PagingSource<Int, T>(), IOperator<P, T> {

    private val cache: MutableList<T> = mutableListOf()
    private fun adjustPageData(listMeta: ListMeta<T>) {
        pageData?.hasMore = listMeta.hasMore
        pageData?.pageNo = (pageData?.pageNo ?: 0) + 1
    }

    fun copy(old: MinePagingSource<P, T>) {
        this.cache.apply {
            clear()
            addAll(old.cache)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        if (pageData?.pageNo == 0) {
            loading.invoke(true)
        }
        if (pageData?.hasMore == false || pageData?.param == null) {
            cache.clear()
            return LoadResult.Page(emptyList(), null, null)
        }
        return withContext(IO) {
            try {
                pageData.let { pageData ->
                    val listMeta = fetcher.fetch(pageData)
                    cache.apply {
                        if (pageData.pageNo == 0) {
                            this.clear()
                        }
                        this.addAll(listMeta.pageList)
                    }
                    adjustPageData(listMeta)
                    loading.invoke(false)
                    initLoadComplete.invoke(true)
                    LoadResult.Page(listMeta.pageList, null,
                        if (listMeta.hasMore) pageData.pageNo else null
                    )
                }
            } catch (e: Exception) {
                loading.invoke(false)
                initLoadComplete.invoke(true)
                LoadResult.Error(e)
            }
        }
    }

    override fun get(): List<T> {
        return cache
    }

}