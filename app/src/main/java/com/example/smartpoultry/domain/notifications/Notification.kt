package com.example.smartpoultry.domain.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.screens.alerts.AlertsActivity

fun createNotificationChannel(context: Context, channelName : String, descriptionText : String, channelID : String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, channelName, importance).apply {
            description = descriptionText
        }
        //register channel
        val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}

fun buildNotification(
context: Context,
channelID: String,
title: String,
contentText : String
) : Notification{
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

    val builder = NotificationCompat.Builder(context,channelID)
        .setSmallIcon(
            if (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)R.drawable.chicken_white else R.drawable.chicken
        )
        .setContentTitle(title)
        .setContentText(contentText)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(onNotificationTap(context))



    return builder.build()
}

fun onNotificationTap(
    context: Context
): PendingIntent{
    //Create an explicit intent for activity in app
    val intent = Intent(context, AlertsActivity::class.java)

    return PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_IMMUTABLE)
}

@SuppressLint("MissingPermission")
fun showNotification(
    context: Context,
    notificationId : Int,
    channelID: String,
    title: String,
    contentText: String
){
    NotificationManagerCompat.from(context)
        .notify(
            notificationId,
            buildNotification(
                context = context,
                channelID = channelID,
                title = title,
                contentText = contentText
            )
        )
}