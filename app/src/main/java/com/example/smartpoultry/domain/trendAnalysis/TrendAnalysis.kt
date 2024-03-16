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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject
import kotlin.properties.Delegates

class TrendAnalysis @Inject constructor(
    private val eggCollectionRepository: EggCollectionRepository,
    private val cellsRepository: CellsRepository,
    private val dataStore: AppDataStore,
){
    //val    : Double = 0.5
    //val CONSUCUTIVE_DAYS : Int = 5

     var THRESHOLD_RATIO by Delegates.notNull<Double>()
     var CONSUCUTIVE_DAYS by Delegates.notNull<Int>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.readData(THRESHOLD_RATIO_KEY).collect{
                THRESHOLD_RATIO = (it.toIntOrNull() ?: 0).toDouble()
            }

        }

        CoroutineScope(Dispatchers.IO).launch {
            dataStore.readData(CONSUCUTIVE_DAYS_KEY).collect(){
                CONSUCUTIVE_DAYS = it.toIntOrNull() ?: 0
            }
        }

        getAllCells()
    }

    //first get all cells
    var listOfAllCells = mutableListOf<Cells>()
    var listOfFlaggedCells = mutableListOf<Cells>()

    fun getAllCells(){
        CoroutineScope(Dispatchers.IO).launch {
            cellsRepository.getAllCells().collect {
                listOfAllCells.addAll(it)
            }
        }
    }


    //perfom analyis for each cell
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun performAnalysis() : Result<List<Cells>>{
        Log.d("cell analysis", "Started...")
        if (listOfAllCells.isNotEmpty()){
            for (cell in listOfAllCells){
                Log.d("Analyzing:","cellID: ${cell.cellId}")
                try {
                    CoroutineScope(Dispatchers.Default).launch {
                        if (flagCell(cell.cellId)) listOfFlaggedCells.add(cell)
                    }

                }catch (e : Exception){
                    Log.d("E exception:","while analyzing cellID: ${cell.cellId}")
                    return Result.failure(e)
                }
            }
         //   return Result.success(listOfFlaggedCells)
        }else{
            Log.d("cell analysis:","list is empty")
        }
        Log.d("cell analysis", "Ended here...")
        return Result.success(listOfFlaggedCells)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun flagCell(cellId : Int ) : Boolean{
        var isUnderPerforming = false
        CoroutineScope(Dispatchers.IO).launch{
            val cellRecordsForPastDays = eggCollectionRepository.getCellEggCollectionForPastDays(
                cellId = cellId,
                startDate = Date(
                    localDateToJavaDate(
                        getDateDaysAgo(10)
                    )
                )
            )
            CoroutineScope(Dispatchers.IO).launch {
                cellRecordsForPastDays.collect{ records->
                    isUnderPerforming = checkConsecutiveUnderPerformance(
                        eggRecords =  records,
                        thresholdRatio = 0.7,
                        consecutiveDays = CONSUCUTIVE_DAYS
                    )
                }
            }


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