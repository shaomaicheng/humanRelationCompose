package com.chenglei.humanrelationbooking.books

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chenglei.humanrelationbooking.books.datasource.NewBooksDataSource
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.utils.use
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BooksViewModel(application: Application):AndroidViewModel(application) {

    val books:MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())

     private val booksDataSource by lazy {
         NewBooksDataSource(getApplication(),viewModelScope)
    }

    fun getBooks() {
        booksDataSource.getBooks()
    }

    private fun updateBooks(new:List<Book>){
        this.books.value = new
    }

    val loading = MutableStateFlow(false)
    val emptyBooks= books.map {
        it.isEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        booksDataSource.observer {
            it?.let {
                it.use({p,books->
                    updateBooks(books)
                }, {e->
                    loading.value = false
                    e.printStackTrace()
                }, loading = {
                    loading.value= true
                })
            }
        }
    }
}
