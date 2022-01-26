package com.ubb.mobile.book.data.remote

import com.ubb.mobile.core.Api
import com.ubb.mobile.book.data.Book
import retrofit2.http.*

object BookApi {
    interface Service {
        @GET("/api/book")
        suspend fun find(): List<Book>

        @GET("/api/book/{id}")
        suspend fun read(@Path("id") bookId: String): Book;

        @Headers("Content-Type: application/json")
        @POST("/api/book")
        suspend fun create(@Body book: Book): Book

        @Headers("Content-Type: application/json")
        @PUT("/api/book/{id}")
        suspend fun update(@Path("id") bookId: String, @Body book: Book): Book
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}