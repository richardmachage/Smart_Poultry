package com.example.smartpoultry.domain.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smartpoultry.R
import com.example.smartpoultry.activity.MainActivity
import com.example.smartpoultry.destinations.AlertScreenDestination

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
    val builder = NotificationCompat.Builder(context,channelID)
        .setSmallIcon(R.drawable.chicken_white)
        .setContentTitle(title)
        .setContentText(contentText)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(onNotificationTap(context))

    return builder.build()
}

fun onNotificationTap(
    context: Context
): PendingIntent{
    //Create an explicit intent for activity in app
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("destination", AlertScreenDestination.route)
    }

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