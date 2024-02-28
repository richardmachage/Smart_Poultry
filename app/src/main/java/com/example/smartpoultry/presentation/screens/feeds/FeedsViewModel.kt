package com.example.smartpoultry.presentation.screens.feeds

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.FeedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val feedsRepository : FeedsRepository
): ViewModel() {

    var selectedDate = mutableStateOf(LocalDate.now())
}