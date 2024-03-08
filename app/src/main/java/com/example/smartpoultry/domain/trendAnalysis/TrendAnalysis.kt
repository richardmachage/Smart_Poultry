package com.example.smartpoultry.domain.trendAnalysis

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

class TrendAnalysis @Inject constructor(
    private val eggCollectionRepository: EggCollectionRepository
) {
    val  THRESHOLD_RATIO  : Double = 0.5
    val CONSUCUTIVE_DAYS : Int = 5

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