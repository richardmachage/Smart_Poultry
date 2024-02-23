package com.example.smartpoultry.presentation.screens.analytics

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor (
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
    private val eggCollectionRepository: EggCollectionRepository,
) : ViewModel() {

    val cells = mutableStateListOf<Cells>()
    val blocks = mutableStateListOf<Blocks>()


    fun getAllCells(): Flow<List<Cells>>{
        return cellsRepository.getAllCells().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
    }

    fun getAllBlocksWithCells():Flow<List<BlocksWithCells>>{
        return  blocksRepository.getBlocksWithCells().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
    }
    fun eggCollectionBetweenDates(cellId : Int, startDate:Date, endDate : Date) : Flow<List<EggCollection>> {
            return eggCollectionRepository.getRecordsForCellBetween(cellId,startDate,endDate).stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList(),
            )

    }

}