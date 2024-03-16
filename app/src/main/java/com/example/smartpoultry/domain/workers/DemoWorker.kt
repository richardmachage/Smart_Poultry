package com.example.smartpoultry.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DemoWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result {
        Log.d(TAG,"Worker started")

        for (i in 0..10){
            Log.d(TAG, i.toString())
        }
        return Result.success()
    }

    companion object{
        const val TAG = "DemoWorker"
    }

}