package com.example.smartpoultry.domain.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AnalysisWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result {
        Log.d("Analysis started", "....." )
        Log.d("Analysis stopped", "....." )

        return Result.success()
    }

}