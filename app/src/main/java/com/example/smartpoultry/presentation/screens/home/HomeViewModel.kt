package com.example.smartpoultry.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import java.util.ArrayList
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val blocksRepository: BlocksRepository,
    private val cellsRepository: CellsRepository,
    private val eggCollectionRepository: EggCollectionRepository,

): ViewModel() {

    val totalBlocks = blocksRepository.getAllBlocks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val totalCells = cellsRepository.getAllCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    var eggCollectionRecords = eggCollectionRepository
        .getRecentEggCollectionRecords(Date(LocalDate.now().toEpochDay())).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )


    @RequiresApi(Build.VERSION_CODES.O)
    fun getEggCollectionRecords(): List<DailyEggCollection> {
        var theList = emptyList<DailyEggCollection>()
        viewModelScope.launch {
            eggCollectionRepository.getRecentEggCollectionRecords(Date(LocalDate.now().toEpochDay())).collect {
                theList = it
            }
        }
        return theList
    }


    private val dateLabels = mapOf(
        0f to "26 Jan",
        1f to "27 Jan",
        2f to "28 Jan",
        3f to "29 Jan",
    )

    val eggRecords = mapOf(
        200f to "200",
        600f to "300",
        400f to "200",
        800f to "500",
    )

    val horizontalAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            dateLabels[value] ?: ""
        }

    val verticalAxisValueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
        eggRecords[value] ?: ""
    }


    @RequiresApi(Build.VERSION_CODES.O)
    val chartEntryModelProducer = ChartEntryModelProducer(getChartEntries())
    //val chartEntryModel = entryModelOf(200f,600f,400f,800f)

    fun getRandomEntries() = List(4) { entryOf(it, Random.nextFloat() * 16f) }

    //var chartEntries = ArrayList<FloatEntry>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getChartEntries() : ArrayList<FloatEntry> {

        val chartEntries = ArrayList<FloatEntry>()
        getEggCollectionRecords().forEachIndexed() { index,record ->
            chartEntries[index] = entryOf(x=record.totalEggs, y = record.date.time)
            println("${record.date} : ${record.totalEggs} eggs")
        }
        return chartEntries
    }
}