package com.forsythe.smartpoultry.utils

fun getTimeAgo(pastTime: Long): String {
    val currentTime = System.currentTimeMillis()
    val differenceInMillis = currentTime - pastTime

    val seconds = differenceInMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "$seconds seconds ago"
        minutes == 1L -> "$minutes minute ago"
        minutes < 60 -> "$minutes minutes ago"
        hours == 1L -> "$hours hour ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "$days day ago"
        else -> "$days days ago"
    }
}