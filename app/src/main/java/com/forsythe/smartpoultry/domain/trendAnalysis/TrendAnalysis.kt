package com.forsythe.smartpoultry.domain.trendAnalysis

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.forsythe.smartpoultry.data.dataSource.local.datastore.AppDataStore
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.domain.repository.CellsRepository
import com.forsythe.smartpoultry.domain.repository.EggCollectionRepository
import com.forsythe.smartpoultry.utils.CONSUCUTIVE_DAYS_KEY
import com.forsythe.smartpoultry.utils.THRESHOLD_RATIO_KEY
import com.forsythe.smartpoultry.utils.localDateToJavaDate
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

class TrendAnalysis @Inject constructor(
    private val eggCollectionRepository: EggCollectionRepository,
    private val cellsRepository: CellsRepository,
    private val dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
) {

    //first get all cells
    var listOfAllCells = mutableListOf<Cells>()

    init {
        getAllCells()
    }


    private fun getThreshHoldRatio(): Float =
        preferencesRepo.loadData(THRESHOLD_RATIO_KEY)!!.toFloatOrNull() ?: 0.0f

    private fun getConsucutiveDays(): Int =
        preferencesRepo.loadData(CONSUCUTIVE_DAYS_KEY)!!.toIntOrNull() ?: 0

    private fun getAllCells() {
        CoroutineScope(Dispatchers.IO).launch {
            cellsRepository.getAllCells().collect {
                listOfAllCells.clear()
                listOfAllCells.addAll(it)
            }
        }
    }

    //perform analysis for each cell
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun performAnalysis(): Result<List<Cells>> = coroutineScope {

        val deferredResults = listOfAllCells.map { cell ->
            async(Dispatchers.Default) {

                if (myFlagCell(cell.cellId).await()) {
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
            val records = eggCollectionRepository.getCellEggCollectionForPastDays(
                cellId = cellId,
                startDate = Date(
                    localDateToJavaDate(getDateDaysAgo(getConsucutiveDays()))
                )
            ).first()

            // { records ->
            var count = 0
            for (record in records) {
                val ratio = record.eggCount.toFloat() / record.henCount.toFloat()
                // Log.d("Compare", "is $ratio < $THRESHOLD_RATIO")
                if (ratio <= getThreshHoldRatio()) {
                    count++
                    if (count >= getConsucutiveDays()) result = true
                }
                else {
                    count = 0
                }
            }
            isUnderPerforming.complete(result)
            //}
        }
        return isUnderPerforming
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateDaysAgo(numberOfDays: Int): LocalDate {
        return LocalDate.now().minusDays(numberOfDays.toLong())
    }
}