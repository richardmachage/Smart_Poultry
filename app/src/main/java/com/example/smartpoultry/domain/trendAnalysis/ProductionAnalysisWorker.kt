package com.example.smartpoultry.domain.trendAnalysis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.smartpoultry.R
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate

@HiltWorker
class ProductionAnalysisWorker @AssistedInject constructor(
    @Assisted appContext : Context,
   @Assisted params: WorkerParameters,
    private val eggCollectionRepository: EggCollectionRepository,
    private val cellsRepository: CellsRepository,
    ) : Worker(appContext, params) {
    var flaggedCells = mutableListOf<Cells>()
    val  THRESHOLD_RATIO  : Double = 0.5
    val CONSUCUTIVE_DAYS : Int = 5


    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {

        }
        return Result.success()
    }

    private fun sendNotification(flaggedCells : List<Cells>){
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a NotificationChannel for API 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel( "flaggedCellsChannel", "Flagged Cells Alerts", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Alerts for cells with downward egg production trend"
            }

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext,"flaggedCellsChannel")
            .setContentTitle("Low production")
            .setContentText("Cells With low production detected")
            .setSmallIcon(R.drawable.chicken)
            .build()

        notificationManager.notify(1, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun flagCell(cellId : Int ) : List<Int>{
        var flaggedCells = mutableListOf<Int>()
        val cellRecordsForPastDays = eggCollectionRepository.getCellEggCollectionForPastDays(
            cellId = cellId,
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

            if(isUnderPerforming) flaggedCells.add(cellId) //add the cell to flagged

        }

        return flaggedCells

    }

    private fun checkConsecutiveUnderPerformance(
        eggRecords: List<EggCollection>, //This list should always be for like the past number of X days specified
        thresholdRatio: Double,
        consecutiveDays: Int
    ): Boolean {
        if (eggRecords.isEmpty() || consecutiveDays <= 0) return false

        // Calculate the ratio of henCount to eggCount for each record
        val ratios = eggRecords.map { record ->
            if (record.eggCount > 0) record.henCount.toDouble() / record.eggCount else 0.0
        }

        // Check for underPerformance over consecutive days
        var count = 0
        for (ratio in ratios) {
            if (ratio < thresholdRatio) {
                count++
                if (count >= consecutiveDays) return true // Found underPerformance for the required consecutive days
            } else {
                count = 0 // Reset count if the performance is above the threshold
            }
        }

        return false // No underPerformance found for the specified consecutive days
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateDaysAgo(numberOfDays: Int): LocalDate {
        return LocalDate.now().minusDays(numberOfDays.toLong())
    }
} 