package com.example.smartpoultry.presentation.screens.splashScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewmodel @Inject constructor(
    val firebaseAuth: FirebaseAuth
): ViewModel() {

}