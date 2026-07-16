package com.kaushalpanjee.CBT.WorkManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

import android.os.Handler
import android.os.Looper
import android.widget.Toast

import kotlinx.coroutines.delay

//SubmitExamWorker add SubmitExamWorker
class SubmitExamWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {

            val candidateId = inputData.getString("candidateId")

            Log.d("API_DEBUG", "1. API CALL START")

            // 👉 YAHAN API CALL KARO
            // val response = repository.submitExam(candidateId!!)

            delay(2000) // test

            Log.d("API_DEBUG", "2. API SUCCESS")

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, "API Success", Toast.LENGTH_SHORT).show()
            }

            Result.success()

        } catch (e: Exception) {

            Log.d("API_DEBUG", "API FAILED: ${e.message}")

            Result.retry()
        }
    }
}
//class SubmitExamWorker(
//    context: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result {
//        return try {
//
//            val candidateId = inputData.getString("candidateId")
//
//            // ✅ Log
//            Log.d("WORK_TEST", "Worker started: $candidateId")
//
//            // ✅ Toast (MAIN THREAD pe run karna zaroori hai)
//            Handler(Looper.getMainLooper()).post {
//                Toast.makeText(
//                    applicationContext,
//                    "Worker Started for $candidateId",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            delay(3000)
//
//            Handler(Looper.getMainLooper()).post {
//                Toast.makeText(
//                    applicationContext,
//                    "Worker Completed",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            Log.d("WORK_TEST", "Worker completed")
//
//            Result.success()
//
//        } catch (e: Exception) {
//
//            Handler(Looper.getMainLooper()).post {
//                Toast.makeText(
//                    applicationContext,
//                    "Worker Failed",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//
//            Result.retry()
//        }
//    }
//}




