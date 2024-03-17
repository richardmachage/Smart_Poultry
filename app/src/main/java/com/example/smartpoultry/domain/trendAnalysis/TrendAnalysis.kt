package com.example.smartpoultry.domain.trendAnalysis

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.presentation.screens.settingsScreen.CONSUCUTIVE_DAYS_KEY
import com.example.smartpoultry.presentation.screens.settingsScreen.THRESHOLD_RATIO_KEY
import com.example.smartpoultry.utils.localDateToJavaDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject
import kotlin.properties.Delegates

class TrendAnalysis @Inject constructor(
    private val eggCollectionRepository: EggCollectionRepository,
    private val cellsRepository: CellsRepository,
    private val dataStore: AppDataStore,
) {

    var THRESHOLD_RATIO by Delegates.notNull<Float>()
    var CONSUCUTIVE_DAYS by Delegates.notNull<Int>()

    //first get all cells
    var listOfAllCells = mutableListOf<Cells>()
    private var listOfFlaggedCells = mutableListOf<Cells>()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.readData(THRESHOLD_RATIO_KEY).collect {
                THRESHOLD_RATIO = it.toFloatOrNull() ?: 0.0f
                Log.d("ratio from datastore", THRESHOLD_RATIO.toString())
            }

        }

        CoroutineScope(Dispatchers.IO).launch {
            dataStore.readData(CONSUCUTIVE_DAYS_KEY).collect() {
                CONSUCUTIVE_DAYS = it.toIntOrNull() ?: 0
            }
        }

        getAllCells()
    }


    private fun getAllCells() {
        CoroutineScope(Dispatchers.IO).launch {
            cellsRepository.getAllCells().collect {
                listOfAllCells.addAll(it)
            }
        }
    }


    //perform analysis for each cell
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun performAnalysis(): Result<List<Cells>> = coroutineScope {
        val deferred = async(Dispatchers.Default) {
            Log.d("cell analysis", "Started...")
            if (listOfAllCells.isNotEmpty()) {
                for (cell in listOfAllCells) {
                    Log.d("Now Analyzing:", "cell: ${cell.cellNum} in Block: ${cell.blockId}")
                    try {
                        if (flagCell(cell.cellId)) {
                            listOfFlaggedCells.add(cell)
                            Log.d(
                                "Flagged state:",
                                "cell ${cell.cellNum} in Block: ${cell.blockId} flagged"
                            )

                        } else {
                            Log.d(
                                "Flagged state:",
                                "cell ${cell.cellNum} in Block: ${cell.blockId} Not flagged"
                            )
                        }
                        //  }.await()

                    } catch (e: Exception) {
                        Log.d("E exception:", "while analyzing cellID: ${cell.cellId}")
                        //return@async Result.failure(e)
                    }
                    Log.d("Finished Analyzing:", "cell: ${cell.cellNum} in Block: ${cell.blockId}")
                }
                return@async Result.success(listOfFlaggedCells)
            } else {
                Log.d("cell analysis:", "list is empty")
            }
            Log.d("cell analysis", "Ended here...")
            return@async Result.success(listOfFlaggedCells)
        }
        deferred.await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun flagCell(cellId: Int): Deferred<Boolean> {
        var isUnderPerforming = false
        CoroutineScope(Dispatchers.IO).launch {
            eggCollectionRepository.getCellEggCollectionForPastDays(
                cellId = cellId,
                startDate = Date(
                    localDateToJavaDate(
                        getDateDaysAgo(10)
                    )
                )
            ).collect { records ->
                CoroutineScope(Dispatchers.Default).launch {
                    isUnderPerforming = checkConsecutiveUnderPerformance(
                        eggRecords = records,
                        thresholdRatio = THRESHOLD_RATIO,
                        consecutiveDays = CONSUCUTIVE_DAYS
                    )
                }
            }
            /* CoroutineScope(Dispatchers.IO).launch {
                 cellRecordsForPastDays.collect{ records->
                     isUnderPerforming = checkConsecutiveUnderPerformance(
                         eggRecords =  records,
                         thresholdRatio = 0.7,
                         consecutiveDays = CONSUCUTIVE_DAYS
                     )
                 }
             }
 */
        }

        return isUnderPerforming

    }

    private fun checkConsecutiveUnderPerformance(
        eggRecords: List<EggCollection>, //This list should always be for like the past number of X days specified
        thresholdRatio: Float,
        consecutiveDays: Int
    ): Boolean {
        if (eggRecords.isEmpty() || consecutiveDays <= 0) return false

        /*val ratios = eggRecords.map { record ->
            if (record.eggCount > 0) record.henCount.toDouble() / record.eggCount else 0.0
        }
*/
        // Check for underPerformance over consecutive days
        var count = 0
        for (record in eggRecords) {
            // Calculate the ratio of eggCount to hen count
            Log.d("eggs", record.eggCount.toString())
            Log.d("hencount", record.henCount.toString())
            //  val ratio = record.henCount.toFloat() / record.eggCount.toFloat()
            val ratio = record.eggCount.toFloat() / record.henCount.toFloat()

            if (ratio < thresholdRatio) {
                Log.d("Compare", "is $ratio < $thresholdRatio")
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