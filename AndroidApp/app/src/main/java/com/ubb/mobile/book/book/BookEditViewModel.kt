package com.ubb.mobile.book.book

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

class BookEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val bookRepository: BookRepository

    init {
        val bookDao = BookDatabase.getDatabase(application, viewModelScope).bookDao()
        bookRepository = BookRepository(bookDao)
    }

    fun getBookById(bookId: String): LiveData<Book> {
        Log.v(TAG, "getBookById...")
        return bookRepository.getById(bookId)
    }

    fun saveOrUpdateBook(book: Book) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateBook...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Book>
            if (book._id.isNotEmpty()) {
                result = bookRepository.update(book)
            } else {
                result = bookRepository.save(book)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateBook succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateBook failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}