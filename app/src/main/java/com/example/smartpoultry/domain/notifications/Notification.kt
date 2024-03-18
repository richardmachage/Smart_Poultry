package com.example.smartpoultry.domain.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.smartpoultry.R

fun createNotificationChannel(context: Context, channelName : String, descriptionText : String, channelID : String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val importance = NotificationManager.IMPORTANCE_DEFAULT
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
    val builder = NotificationCompat.Builder(context,channelID)
        .setSmallIcon(R.drawable.chicken_white)
        .setContentTitle(title)
        .setContentText(contentText)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    return builder.build()
}