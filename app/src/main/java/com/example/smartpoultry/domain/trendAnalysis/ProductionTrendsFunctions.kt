package com.example.smartpoultry.domain.trendAnalysis

import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection

fun checkConsecutiveUnderPerformance(
    eggRecords: List<EggCollection>, //This list should always be for like the past number of X days specified
    thresholdRatio: Double,
    consecutiveDays: Int
): Boolean {
    if (eggRecords.isEmpty() || consecutiveDays <= 0) return false

    // Calculate the ratio of henCount to eggCount for each record
    val ratios = eggRecords.map { record ->
        if (record.eggCount > 0) record.henCount.toDouble() / record.eggCount else 0.0
    }

    // Check for underPerformance over consecutive days
    var count = 0
    for (ratio in ratios) {
        if (ratio < thresholdRatio) {
            count++
            if (count >= consecutiveDays) return true // Found underPerformance for the required consecutive days
        } else {
            count = 0 // Reset count if the performance is above the threshold
        }
    }

    return false // No underPerformance found for the specified consecutive days
}

