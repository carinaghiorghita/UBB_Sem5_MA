package com.ubb.mobile.book.data
//
//import android.util.Log
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.lifecycleScope
//import com.ubb.mobile.book.data.remote.BookApi
//import com.ubb.mobile.core.Properties
//import com.ubb.mobile.core.TAG
//import kotlinx.coroutines.launch
//import com.ubb.mobile.core.Result
//
//
//object BookRepoHelper {
//    var bookRepository: BookRepository? = null
//    private var book: Book? = null
//    private var viewLifecycleOwner: LifecycleOwner? = null
//
//    fun setbookRepo(bookParam: BookRepository) {
//        this.bookRepository = bookParam
//    }
//
//    fun setbook(bookParam: Book) {
//        this.book = bookParam
//    }
//
//    fun setViewLifecycleOwner(viewLifecycleOwnerParam: LifecycleOwner) {
//        viewLifecycleOwner = viewLifecycleOwnerParam
//    }
//
//    fun save() {
//        viewLifecycleOwner!!.lifecycleScope.launch {
//            saveHelper()
//        }
//    }
//
//    private suspend fun saveHelper(): Result<Book> {
//        try {
//            if (Properties.instance.internetActive.value!!) {
//
//                val createdbook = BookApi.service.create(book!!)
//
//                bookRepository!!.bookDao.insert(createdbook)
//                Properties.instance.toastMessage.postValue("book was saved on the server")
//                return Result.Success(createdbook)
//            } else {
//                Log.d(TAG, "internet still not working...")
//                return Result.Error(Exception("internet still not working..."))
//            }
//        } catch (e: Exception) {
//            return Result.Error(e)
//        }
//    }
//
//    fun update() {
//        viewLifecycleOwner!!.lifecycleScope.launch {
//            updateHelper()
//        }
//    }
//
//    private suspend fun updateHelper(): Result<Book> {
//        try {
//            if (Properties.instance.internetActive.value!!) {
//                Log.d(TAG, "updateNewVersionHelper")
//                val updatedbook = BookApi.service.update(book!!._id, book!!)
//                bookRepository!!.bookDao.update(updatedbook)
//                Properties.instance.toastMessage.postValue("book was updated on the server")
//                return Result.Success(updatedbook)
//            } else {
//                Log.d(TAG, "internet still not working...")
//                return Result.Error(Exception("internet still not working..."))
//            }
//        } catch (e: Exception) {
//            return Result.Error(e)
//        }
//    }
//}