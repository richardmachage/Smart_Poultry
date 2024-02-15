package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EggScreenViewModel @Inject constructor (
    private val blocksRepository: BlocksRepository,
    private val cellsRepository: CellsRepository,
    private val eggCollectionRepository: EggCollectionRepository
): ViewModel(){
    var selectedDate = mutableStateOf(LocalDate.now())
        private set

    var totalEggCount = mutableStateOf("")
        private set

    fun setTotalEggCount(totalEggs : Int) {
        totalEggCount.value = totalEggs.toString()
    }

    fun setSelectedDate(date: LocalDate){
        selectedDate.value = date
    }

}