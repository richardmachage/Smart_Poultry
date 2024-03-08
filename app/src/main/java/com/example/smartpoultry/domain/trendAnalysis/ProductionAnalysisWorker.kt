package com.example.smartpoultry.domain.trendAnalysis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartpoultry.R
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.sql.Date
import java.time.LocalDate

@HiltWorker
class ProductionAnalysisWorker @AssistedInject constructor(
    @Assisted appContext : Context,
   @Assisted params: WorkerParameters,
    private val eggCollectionRepository: EggCollectionRepository,
    private val cellsRepository: CellsRepository,
    ) : CoroutineWorker(appContext, params) {
    val  THRESHOLD_RATIO  : Double = 0.5
    val CONSUCUTIVE_DAYS : Int = 5


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        //CoroutineScope(Dispatchers.IO).launch {
            //we first get the list of all cells
            var listOfAllCells = mutableListOf<Cells>()
            var listOfFlaggedCells = mutableListOf<Cells>()
            cellsRepository.getAllCells().collect{
                listOfAllCells.addAll(it)
            }

            //then for each cell we perform the analysis
        if (listOfAllCells.isNotEmpty()) {
            for (cell in listOfAllCells) {
                try {
                if (flagCell(cell.cellId)) listOfFlaggedCells.add(cell)
                }catch (e : Exception){
                    return Result.failure()
                }
            }
        }
            if (listOfFlaggedCells.isNotEmpty()) {
                sendNotification(listOfFlaggedCells)
            }

        //}
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

        notificationManager.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun flagCell(cellId : Int ) : Boolean{
        var isUnderPerforming = false
        val cellRecordsForPastDays = eggCollectionRepository.getCellEggCollectionForPastDays(
            cellId = cellId,
            startDate = Date(
                localDateToJavaDate(
                    getDateDaysAgo(10)
                )
            )
        )
        cellRecordsForPastDays.collect{ records->
            isUnderPerforming = checkConsecutiveUnderPerformance(
                eggRecords =  records,
                thresholdRatio = THRESHOLD_RATIO,
                consecutiveDays = CONSUCUTIVE_DAYS
            )


        }

        return isUnderPerforming

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