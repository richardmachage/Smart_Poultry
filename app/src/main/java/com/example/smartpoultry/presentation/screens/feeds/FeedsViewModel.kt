package com.example.smartpoultry.presentation.screens.feeds

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.feeds.FeedTrack
import com.example.smartpoultry.data.dataSource.room.entities.feeds.Feeds
import com.example.smartpoultry.domain.repository.FeedsRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val feedsRepository: FeedsRepository
) : ViewModel() {

    var recordsNumOfSacks = mutableIntStateOf(0)
    var feedTrackNumOfSacks = mutableIntStateOf(0)

    @RequiresApi(Build.VERSION_CODES.O)
    var recordSelectedDate = mutableStateOf(LocalDate.now())

    var feedTrackSelectedDate = mutableStateOf(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    var searchDate = mutableStateOf(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSearchFeedRecord(): Flow<List<Feeds>> {
        return feedsRepository.getFeedsRecord(
            Date(localDateToJavaDate(searchDate.value))
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun onAddFeedRecord(): Long {

        return feedsRepository.addNewFeedsRecord(
            Feeds(
                date = Date(localDateToJavaDate(recordSelectedDate.value)),
                numOfSacks = recordsNumOfSacks.intValue
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun onAddFeeTrackRecord(): Long{
        return  feedsRepository.addNewFeedTrackRecord(
            FeedTrack(
                date = Date(localDateToJavaDate(feedTrackSelectedDate.value)),
                numOfSacks = feedTrackNumOfSacks.intValue
            )
        )
    }
}