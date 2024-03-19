package com.example.smartpoultry.presentation.screens.alerts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataModels.AlertFull
import com.example.smartpoultry.domain.repository.AlertsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val alertsRepository: AlertsRepository
) : ViewModel() {

    var selectedAlertId by mutableIntStateOf(0)
    fun getFlaggedCells(): Flow<List<AlertFull>>{
        return alertsRepository.getFlaggedCells().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    }

    fun onDeleteAlert(alertId : Int){
        viewModelScope.launch {
            alertsRepository.deleteAlert(alertId)
        }
    }

    fun onMarkAttended(status:Boolean, alertId : Int){
        viewModelScope.launch {
            alertsRepository.updateAttendedStatus(status,alertId)
        }
    }
}