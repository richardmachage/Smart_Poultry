package com.forsythe.smartpoultry.presentation.screens.viewRecordsScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.data.dataModels.EggRecordFull
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.domain.reports.createWorkBook
import com.forsythe.smartpoultry.domain.reports.saveWorkbookWithMediaStore
import com.forsythe.smartpoultry.domain.repository.CellsRepository
import com.forsythe.smartpoultry.domain.repository.EggCollectionRepository
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

    lateinit var cellsMap : Map<Int, Cells>
    var searchText = mutableStateOf("")
    var listOfCollectionRecords = mutableListOf<EggRecordFull>()
    var toastMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

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

    fun onDeleteRecord(recordId:Int){
        viewModelScope.launch {
            eggCollectionRepository.deleteRecord(recordId)
        }
    }

    fun onExportToExcel(context: Context, listOfRecords : List<EggRecordFull>) {
        viewModelScope.launch {
            try {

                isLoading.value = true


                val workbook = createWorkBook(
                    context = context,
                    sheetName = "Egg Collection",
                    listOfRecords = listOfRecords
                )

                Log.d("export Excel", "save workbook")
                saveWorkbookWithMediaStore(
                    context = context,
                    workbook = workbook,
                    fileName = "Eggs Collected"
                )

                Log.d("export Excel", "finish saving workbook")

                isLoading.value = false
                toastMessage.value = "Excel Exported to downloads"

            }catch (e:Exception){
                isLoading.value = false
                toastMessage.value = e.localizedMessage?.toString() ?: "Error: Failed to save file"
            }
        }
    }
}