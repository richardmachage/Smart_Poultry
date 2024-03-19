package com.example.smartpoultry.domain.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smartpoultry.data.dataSource.room.entities.alerts.Alerts
import com.example.smartpoultry.domain.notifications.showNotification
import com.example.smartpoultry.domain.repository.AlertsRepository
import com.example.smartpoultry.domain.trendAnalysis.TrendAnalysis
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

@HiltWorker
class AnalysisWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val trendAnalysis: TrendAnalysis,
    private val alertsRepository: AlertsRepository
) : CoroutineWorker(context, workerParameters) {

   /* private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
*/
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        Log.d("Analysis worker: ", "started")
        Log.d("Threshold ratio: ", trendAnalysis.THRESHOLD_RATIO.toString())
        Log.d("ConsucutiveDays: ", trendAnalysis.CONSUCUTIVE_DAYS.toString())
        Log.d("Cells ", trendAnalysis.listOfAllCells.size.toString())

        val result = trendAnalysis.performAnalysis()
        result.onSuccess {flaggedCells->
            //TODO save the flagged to database, call for notification
            Log.d("on success:", "Flagged cells ${flaggedCells.size}")

            CoroutineScope(Dispatchers.IO).launch {
                for (cell in flaggedCells){
                    alertsRepository.addAlert(
                        Alerts(
                            date = Date(System.currentTimeMillis()),
                            flaggedCellId = cell.cellId,
                        )
                    )
                }
            }

            showNotification(
                context = context,
                notificationId = 1,
                channelID = "1",
                title = "Analysis Complete",
                contentText = "Downward trend detected in ${flaggedCells.size} cells"
            )
        }
        result.onFailure {
            Log.d("on Failure:", "${it.message}")
        }
        Log.d("Analysis worker: ", "ended")

        //notificationManager?.cancel(NOTIFICATION_ID)
        return Result.success()
    }

}