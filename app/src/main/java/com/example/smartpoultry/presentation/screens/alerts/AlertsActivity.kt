package com.example.smartpoultry.presentation.screens.alerts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smartpoultry.presentation.screens.mainActivity.MainActivity
import com.example.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartPoultryTheme {
                AlertScreen()
            }
        }
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

