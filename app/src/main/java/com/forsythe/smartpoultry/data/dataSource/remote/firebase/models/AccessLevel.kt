package com.forsythe.smartpoultry.data.dataSource.remote.firebase.models

data class AccessLevel(
    var collectEggs : Boolean = false,
    var editHenCount : Boolean = false,
    var manageBlocksCells: Boolean = false,
    var manageUsers : Boolean = false
)
