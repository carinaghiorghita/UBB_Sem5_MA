package com.ubb.mobile.book.books

import android.content.Context
import android.net.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ubb.mobile.R
import com.ubb.mobile.auth.data.AuthRepository
//import com.ubb.mobile.book.data.BookRepoHelper
//import com.ubb.mobile.book.data.BookRepoWorker
import com.ubb.mobile.core.Properties
import com.ubb.mobile.core.TAG
import com.ubb.mobile.databinding.FragmentBookListBinding


class BookListFragment : Fragment() {
    private var _binding: FragmentBookListBinding? = null
    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var booksModel: BooksListViewModel
    private lateinit var connectivityManager: ConnectivityManager
    //private lateinit var connectivityLiveData: ConnectivityLiveData
    private val binding get() = _binding!!

//    override fun OnCreate(savedInstanceState: Bundle) {
//        super.onCreate(savedInstanceState)
//        connectivityManager = activity?.getSystemService(android.net.ConnectivityManager::class.java)!!
//        connectivityLiveData = ConnectivityLiveData(connectivityManager)
//        connectivityLiveData.observe(this, {
//            Log.d(TAG, "connectivityLiveData $it")
//        })
//    }
//
//    override fun onStart() {
//        super.onStart()
//        Log.d(TAG, "isOnline ${isOnline()}")
//        connectivityManager.registerDefaultNetworkCallback(networkCallback)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        connectivityManager.unregisterNetworkCallback(networkCallback)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        _binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
//        BookRepoHelper.setViewLifecycleOwner(viewLifecycleOwner)
//        Properties.instance.internetActive.observe(viewLifecycleOwner, Observer {
//            Log.d(TAG, "sending offline actions to server")
//            //sendOfflineActionsToServer()
//            })
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.FragmentLogin)
            return;
        }
        setupBookList()
        binding.fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.action_BookListFragment_to_BookEditFragment)
        }
    }

    private fun setupBookList() {
        bookListAdapter = BookListAdapter(this)
        binding.itemList.adapter = bookListAdapter
        booksModel = ViewModelProvider(this).get(BooksListViewModel::class.java)
        booksModel.books.observe(viewLifecycleOwner, { value ->
            Log.i(TAG, "update books")
            bookListAdapter.books = value
        })
        booksModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        booksModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        booksModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }

//    private fun sendOfflineActionsToServer() {
//        val books = booksModel.bookRepository.bookDao.getAll()
//        books.value?.forEach { book ->
//            Log.d(TAG, "Send ${book.title} to server")
//            BookRepoHelper.setbook(book)
//            var dataParam = Data.Builder().putString("operation", "update")
//            val request = OneTimeWorkRequestBuilder<BookRepoWorker>()
//                .setInputData(dataParam.build())
//                .build()
//            WorkManager.getInstance(requireContext()).enqueue(request)
//        }
//    }
}
