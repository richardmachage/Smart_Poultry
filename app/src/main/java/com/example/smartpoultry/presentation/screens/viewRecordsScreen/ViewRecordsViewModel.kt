package com.example.smartpoultry.presentation.screens.viewRecordsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataModels.EggRecordFull
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewRecordsViewModel @Inject constructor(
    private val eggCollectionRepository: EggCollectionRepository,
    private val cellsRepository: CellsRepository
) : ViewModel() {

    lateinit var cellsMap : Map<Int,Cells>
    var searchText = mutableStateOf("")
    var listOfCollectionRecords = mutableListOf<EggRecordFull>()

    init {
        viewModelScope.launch {
            cellsRepository.getAllCells().collect{
                cellsMap = it.associateBy {  it.cellId}
            }
        }

        viewModelScope.launch {
            eggCollectionRepository.getAllFullEggCollection().collect{
                listOfCollectionRecords.addAll(it)
            }
        }
    }
    fun getAllRecords (): Flow<List<EggCollection>> {
        return eggCollectionRepository.getAllRecords()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )
    }

    fun getAllFullRecords (): Flow<List<EggRecordFull>> {
        return eggCollectionRepository.getAllFullEggCollection()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )
    }
    fun getCell(cellId : Int): Cells?{
        return cellsMap[cellId]
    }

    fun searchRecord(searchQuery : String, list : List<EggRecordFull>) : List<EggRecordFull> {
        return if (searchQuery.isNotBlank()) list.filter { record->
            record.doesMatchSearchQuery(searchQuery)
        } else emptyList()
    }
}