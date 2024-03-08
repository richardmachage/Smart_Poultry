package com.example.smartpoultry.domain.trendAnalysis

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.assisted.Assisted
import java.sql.Date

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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun flagCell(cell : Cells) : List<Cells>{
        var flaggedCells = mutableListOf<Cells>()
        val cellRecordsForPastDays = eggCollectionRepository.getCellEggCollectionForPastDays(
            cellId = cell.cellId,
            startDate = Date(
                localDateToJavaDate(
                    getDateDaysAgo(10)
                )
            )
        )
        cellRecordsForPastDays.collect{ records->
            val isUnderPerforming = checkConsecutiveUnderPerformance(
                eggRecords =  records,
                thresholdRatio = THRESHOLD_RATIO,
                consecutiveDays = CONSUCUTIVE_DAYS
            )

            if(isUnderPerforming) flaggedCells.add(cell) //add the cell to flagged

        }

        return flaggedCells

    }
} 