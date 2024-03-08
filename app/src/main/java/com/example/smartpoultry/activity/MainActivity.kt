package com.example.smartpoultry.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartpoultry.domain.trendAnalysis.ProductionAnalysisWorker
import com.example.smartpoultry.presentation.NavGraphs
import com.example.smartpoultry.presentation.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   // val mainViewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val workRequest = PeriodicWorkRequestBuilder<ProductionAnalysisWorker>(24,TimeUnit.HOURS).build()

       WorkManager.getInstance(applicationContext).enqueue(workRequest)

        super.onCreate(savedInstanceState)
        setContent {
            SmartPoultryTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }


}

