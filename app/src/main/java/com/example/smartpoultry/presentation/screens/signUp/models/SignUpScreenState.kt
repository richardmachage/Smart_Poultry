package com.example.smartpoultry.presentation.screens.signUp.models

data class SignUpScreenState(
    var currentPart : SignUpParts,
    var showPrevious : Boolean,
    var showContinue : Boolean,
    var isLoading : Boolean = false
)
