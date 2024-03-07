package com.example.smartpoultry.domain.trendAnalysis

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ProductionAnalysisWorker (
    private  val  appContext : Context,
   private  val params: WorkerParameters
    ) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return Result.success()
    }

} 