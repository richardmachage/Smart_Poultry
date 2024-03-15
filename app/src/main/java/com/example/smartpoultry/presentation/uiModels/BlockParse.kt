package com.example.smartpoultry.presentation.uiModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlockParse(
    val blockId : Int,
    val blockNum : Int,
    val totalCells : Int
): Parcelable
