package com.forsythe.smartpoultry.presentation.screens.signUp.models

data class SignUpScreenState(
    var currentPart : SignUpParts,
    var showPrevious : Boolean,
    var showContinue : Boolean,
    var isLoading : Boolean = false,
    var continueEnabled : Boolean = false,
)
