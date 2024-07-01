package com.example.smartpoultry.data.dataSource.remote.firebase.models

data class AccessLevel(
    val collectEggs : Boolean = false,
    val editHenCount : Boolean = false,
    val manageBlocksCells: Boolean = false,
    val manageUsers : Boolean = false
)
