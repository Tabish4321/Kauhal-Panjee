package com.kaushalpanjee.CBT.WorkManager

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf




fun startSubmitWorker(context: Context, candidateId: String) {

    val data = workDataOf("candidateId" to candidateId)

    val request = OneTimeWorkRequestBuilder<SubmitExamWorker>()
        .addTag("submit_exam") // 👈 important for tracking
        .setInputData(data)
        .build()

    WorkManager.getInstance(context).enqueue(request)
}

//fun startSubmitWorker(context: Context, candidateId: String) {
//
//    val data = workDataOf("candidateId" to candidateId)
//
//    val constraints = Constraints.Builder()
//        .setRequiredNetworkType(NetworkType.CONNECTED)
//        .build()
//
//    val workRequest = OneTimeWorkRequestBuilder<SubmitExamWorker>()
//        .setInputData(data)
//        .setConstraints(constraints)
//        .build()
//
//    WorkManager.getInstance(context).enqueue(workRequest)
//}