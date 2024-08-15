package com.forsythe.smartpoultry.presentation.screens.manageUsers.registerUser.components

data class RegisterUserScreenState(
    var isLoading : Boolean ,//= false,
    var currentPart : RegisterUserParts ,//= RegisterUserParts.PERSONAL_DETAILS,
    var showContinue : Boolean ,//= true,
    var showPrevious : Boolean ,//= false
    var isContinueEnabled : Boolean,
    var toastMessage : String = "",
    var navigateToManageUsers: Boolean = false
)
