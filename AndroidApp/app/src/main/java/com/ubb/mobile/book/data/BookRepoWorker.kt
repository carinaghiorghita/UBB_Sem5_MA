package com.ubb.mobile.book.data

//import android.content.Context
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//
//class BookRepoWorker(context: Context, workerParams: WorkerParameters) :
//    Worker(context, workerParams) {
//    override fun doWork(): Result {
//        when (inputData.getString("operation")) {
//            "save" -> BookRepoHelper.save()
//            "update" -> BookRepoHelper.update()
//            else -> return Result.failure()
//        }
//        return Result.success()
//    }
//}