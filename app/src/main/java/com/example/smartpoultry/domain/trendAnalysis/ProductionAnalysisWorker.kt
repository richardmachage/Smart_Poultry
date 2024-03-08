package com.example.smartpoultry.domain.trendAnalysis

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import dagger.assisted.Assisted

@HiltWorker
class ProductionAnalysisWorker (
    @Assisted appContext : Context,
   @Assisted params: WorkerParameters,
    private val eggCollectionRepository: EggCollectionRepository
    ) : CoroutineWorker(appContext, params) {

    val  THRESHOLD_RATIO  : Double = 0.5
    val CONSUCUTIVE_DAYS : Int = 5

    override suspend fun doWork(): Result {



        return Result.success()
    }

} 