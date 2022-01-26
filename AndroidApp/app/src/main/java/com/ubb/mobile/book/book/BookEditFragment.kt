package com.ubb.mobile.book.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ubb.mobile.R
import com.ubb.mobile.core.TAG
import com.ubb.mobile.databinding.FragmentBookEditBinding
import com.ubb.mobile.book.data.Book

class BookEditFragment : Fragment() {
    companion object {
        const val BOOK_ID = "BOOK_ID"
    }

    private lateinit var viewModel: BookEditViewModel
    private var bookId: String? = null
    private var book: Book? = null

    private var _binding: FragmentBookEditBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        arguments?.let {
            if (it.containsKey(BOOK_ID)) {
                bookId = it.getString(BOOK_ID).toString()
            }
        }
        _binding = FragmentBookEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setupViewModel()
        binding.fab.setOnClickListener {
            Log.v(TAG, "save book")
            val i = book
            if (i != null) {
                i.title = binding.bookTitle.text.toString()
                i.author = binding.bookAuthor.text.toString()
                i.publishingDate = binding.bookPublishingDate.text.toString()
                i.owned = binding.bookOwned.isChecked
                viewModel.saveOrUpdateBook(i)
            }
        }
        binding.bookTitle.setText(bookId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG, "onDestroyView")
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(BookEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            binding.progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigate(R.id.action_BookEditFragment_to_BookListFragment)
            }
        })
        val id = bookId
        if (id == null) {
            book = Book("", "","","",false)
        } else {
            viewModel.getBookById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update books")
                if (it != null) {
                    book = it
                    binding.bookTitle.setText(it.title)
                    binding.bookAuthor.setText(it.author)
                    binding.bookPublishingDate.setText(it.publishingDate)
                    binding.bookOwned.isChecked = it.owned
                }
            })
        }
    }
}