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
    @Assisted val context : Context,
    @Assisted workerParameters: WorkerParameters,
    private val trendAnalysis: TrendAnalysis
) : CoroutineWorker(context, workerParameters){

    //private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        //makeStatusNotification("Analysis started: ", context)
        Log.d("Analysis worker: ", "started")
        Log.d("Threshold ratio: ", trendAnalysis.THRESHOLD_RATIO.toString())
        Log.d("ConsucutiveDays: ", trendAnalysis.CONSUCUTIVE_DAYS.toString())
        Log.d("Cells ", trendAnalysis.listOfAllCells.size.toString())

        val result = trendAnalysis.performAnalysis()
        result.onSuccess {
            //TODO save the flagged to database, call for notification
            Log.d("on success:","Flagged cells ${it.size}")
        }
        result.onFailure {
            Log.d("on Failure:", "${it.message}")
        }
        Log.d("Analysis worker: ", "ended")

        //notificationManager?.cancel(NOTIFICATION_ID)
        return Result.success()
    }

}