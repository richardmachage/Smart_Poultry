package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class EggScreenViewModel : ViewModel(){
    var selectedDate = mutableStateOf(LocalDate.now())
        private set

    fun setSelectedDate(date: LocalDate){
        selectedDate.value = date
    }
}