package com.example.smartpoultry.presentation.screens.home

import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val blocksRepository: BlocksRepository
): ViewModel() {

    private val dateLabels = mapOf(
        0f to "26 Jan",
        1f to "27 Jan",
        2f to "28 Jan",
        3f to "29 Jan",
    )

    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom>{value, _ ->
        dateLabels[value]?: ""
    }

    val chartEntryModel = entryModelOf(200f,600f,400f,800f)

}