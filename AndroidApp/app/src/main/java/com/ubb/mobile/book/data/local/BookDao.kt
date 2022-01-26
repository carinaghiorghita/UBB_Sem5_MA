package com.ubb.mobile.book.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ubb.mobile.book.data.Book

@Dao
interface BookDao {
    @Query("SELECT * from books ORDER BY title ASC")
    fun getAll(): LiveData<List<Book>>

    @Query("SELECT * from books ORDER BY title ASC")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books WHERE _id=:id ")
    fun getById(id: String): LiveData<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(book: Book)

    @Query("DELETE FROM books")
    suspend fun deleteAll()
}