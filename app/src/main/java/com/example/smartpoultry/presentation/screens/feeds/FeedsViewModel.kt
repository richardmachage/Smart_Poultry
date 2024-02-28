package com.example.smartpoultry.presentation.screens.feeds

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.room.entities.feeds.Feeds
import com.example.smartpoultry.domain.repository.FeedsRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val feedsRepository : FeedsRepository
): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    var selectedDate = mutableStateOf(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    var searchDate = mutableStateOf(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSearchFeedRecord(date : LocalDate) : Flow<List<Feeds>>{
        return feedsRepository.getFeedsRecord(
            Date( localDateToJavaDate(searchDate.value))
        )
    }
}