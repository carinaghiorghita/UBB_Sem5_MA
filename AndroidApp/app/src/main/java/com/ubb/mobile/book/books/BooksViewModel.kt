package com.ubb.mobile.book.books

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ubb.mobile.core.Result
import com.ubb.mobile.core.TAG
import com.ubb.mobile.book.data.Book
import com.ubb.mobile.book.data.BookRepository
import com.ubb.mobile.book.data.local.BookDatabase
import kotlinx.coroutines.launch

class BooksListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val books: LiveData<List<Book>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val bookRepository: BookRepository

    init {
        val bookDao = BookDatabase.getDatabase(application, viewModelScope).bookDao()
        bookRepository = BookRepository(bookDao)
        books = bookRepository.books
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = bookRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }

//    fun sendLocalChangesToServer() {
//        viewModelScope.launch {
//            Log.v(TAG, "sending local changes to the server...")
//            mutableLoading.value = true
//            mutableException.value = null
//            bookRepository.sendLocalChangesToServer()
//            mutableLoading.value = false
//        }
//    }
}