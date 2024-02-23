package com.example.smartpoultry.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor (
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
    private val eggCollectionRepository: EggCollectionRepository,
) : ViewModel() {



}