package com.example.smartpoultry.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.random.Random

class AnalyticsViewModel : ViewModel() {

    var listOfEntries = getRandomEntries()
    fun getRandomEntries() = List(4) { entryOf(it, Random.nextFloat() * 16f) }


}