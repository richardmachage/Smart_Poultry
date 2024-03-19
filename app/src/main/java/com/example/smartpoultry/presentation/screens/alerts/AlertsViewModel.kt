package com.example.smartpoultry.presentation.screens.alerts

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.AlertsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val alertsRepository: AlertsRepository
) : ViewModel() {

}