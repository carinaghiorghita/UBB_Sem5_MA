package com.ubb.mobile.book.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "publishingDate") var publishingDate: String,
    @ColumnInfo(name = "owned") var owned: Boolean
) {

    override fun toString(): String {
        var ownedText = if (owned) "" else " don't"
        return "$title by $author, published $publishingDate. You$ownedText own this book."
    }
}
