package com.example.smartpoultry.domain.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartpoultry.domain.trendAnalysis.TrendAnalysis
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AnalysisWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted workerParameters: WorkerParameters,
    private val trendAnalysis: TrendAnalysis
) : CoroutineWorker(context, workerParameters){
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        Log.d("Analysis worker: ", "started")
        Log.d("Threshold ratio: ", trendAnalysis.THRESHOLD_RATIO.toString())
        Log.d("ConsucutiveDays: ", trendAnalysis.CONSUCUTIVE_DAYS.toString())
        Log.d("Cells ", trendAnalysis.listOfAllCells.size.toString())

        val result = trendAnalysis.performAnalysis()
        result.onSuccess {
            Log.d("on success:","Flagged cells ${it.size}")
        }
        result.onFailure {
            Log.d("on Failure:", "${it.message}")
        }

        Log.d("Analysis worker: ", "ended")


        return Result.success()
    }

}