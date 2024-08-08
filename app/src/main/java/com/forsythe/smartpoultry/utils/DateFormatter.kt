package com.forsythe.smartpoultry.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun myDateFormatter(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("dd/mm/yyyy").format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDateDaysAgo(numberOfDays: Int): LocalDate {
    return LocalDate.now().minusDays(numberOfDays.toLong())
}

@RequiresApi(Build.VERSION_CODES.O)
fun localDateToJavaDate(localDate: LocalDate): Long {
    val startOfDayUtc = localDate.atStartOfDay(ZoneId.of("UTC"))

    return startOfDayUtc.toInstant().toEpochMilli()
}

@SuppressLint("SimpleDateFormat")
fun toGraphDate(date: java.sql.Date): String {
    return SimpleDateFormat("dd-MMM").format(date)
}

@SuppressLint("SimpleDateFormat")
fun toGraphDate(date: java.util.Date): String {
    return SimpleDateFormat("dd-MMM").format(date)
}

fun toYearMonth(year: String, month: String): String {
    return when (month) {
        "January" -> "$year-01"
        "February" -> "$year-02"
        "March" -> "$year-03"
        "April" -> "$year-04"
        "May" -> "$year-05"
        "June" -> "$year-06"
        "July" -> "$year-07"
        "August" -> "$year-08"
        "September" -> "$year-09"
        "October" -> "$year-10"
        "November" -> "$year-11"
        "December" -> "$year-12"
        else -> ""
    }
}
