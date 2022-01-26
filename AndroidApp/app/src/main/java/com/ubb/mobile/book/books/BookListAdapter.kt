package com.ubb.mobile.book.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ubb.mobile.R
import com.ubb.mobile.core.TAG
import com.ubb.mobile.book.data.Book
import com.ubb.mobile.book.book.BookEditFragment

class BookListAdapter(
    private val fragment: Fragment,
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    var books = emptyList<Book>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener = View.OnClickListener { view ->
        val book = view.tag as Book
        fragment.findNavController().navigate(R.id.action_BookListFragment_to_BookEditFragment, Bundle().apply {
            putString(BookEditFragment.BOOK_ID, book._id)
        })
    };

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val book = books[position]
        holder.textView.text = book.toString()
        holder.itemView.tag = book
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = books.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.text)
        }
    }
}
