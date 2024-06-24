package com.example.smartpoultry.data.dataSource.remote.firebase.models

data class AccessLevel(
    val collectEggs : Boolean = false,
    val editHenCount : Boolean = false,
    val addNewCell : Boolean = false,
    val deleteCell: Boolean = false,
    val addNewBlock: Boolean = false,
    val deleteBlock : Boolean = false,
    val editBlockName : Boolean = false,
    val manageUsers : Boolean = false
)
