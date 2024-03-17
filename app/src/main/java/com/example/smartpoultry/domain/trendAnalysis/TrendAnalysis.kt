package com.example.smartpoultry.domain.trendAnalysis

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.presentation.screens.settingsScreen.CONSUCUTIVE_DAYS_KEY
import com.example.smartpoultry.presentation.screens.settingsScreen.THRESHOLD_RATIO_KEY
import com.example.smartpoultry.utils.localDateToJavaDate
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    //private var listOfFlaggedCells = mutableListOf<Cells>()


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

        val deferredResults = listOfAllCells.map { cell ->
            async(Dispatchers.Default) {
               // if (flagCell(cell.cellId).await())
                if(myFlagCell(cell.cellId).await())
                {
                    cell
                } else null
            }
        }

        val flaggedCells = deferredResults.awaitAll().filterNotNull()
        Result.success(flaggedCells)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun myFlagCell(cellId: Int): Deferred<Boolean> {
        val isUnderPerforming = CompletableDeferred<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            var result = false
            Log.d("Analysis", "started cell $cellId")
            eggCollectionRepository.getCellEggCollectionForPastDays(
                cellId= cellId,
                startDate = Date(
                    localDateToJavaDate(getDateDaysAgo(10))
                )
            ).collect{records->
                var count = 0
                for (record in records){
                    val ratio = record.eggCount.toFloat() / record.henCount.toFloat()
                    Log.d("Compare", "is $ratio < $THRESHOLD_RATIO")
                    if (ratio <= THRESHOLD_RATIO){
                        count++
                        if (count >= CONSUCUTIVE_DAYS) result = true
                    }else{
                        count = 0
                    }
                }
                Log.d("Flag status", "isFlagged $result")
                isUnderPerforming.complete(result)
            }

        }
        return isUnderPerforming
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateDaysAgo(numberOfDays: Int): LocalDate {
        return LocalDate.now().minusDays(numberOfDays.toLong())
    }
}