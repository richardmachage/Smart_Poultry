package com.forsythe.smartpoultry.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDate.format(pattern : String? = null) : String{
    return  pattern?.let {it->
        DateTimeFormatter.ofPattern(it).format(this)
    }?: run {
        DateTimeFormatter.ofPattern("dd/mm/yyyy").format(this)
    }
}


@SuppressLint("SimpleDateFormat")
fun java.util.Date.format(pattern : String? = null) : String{
    return  pattern?.let {
        SimpleDateFormat(pattern).format(this)
    }?: run {
       SimpleDateFormat("dd/mm/yyyy").format(this)
    }
}

fun myDateFormatter(date: LocalDate): String {
    return DateTimeFormatter.ofPattern("dd/mm/yyyy").format(date)
}

fun getDateDaysAgo(numberOfDays: Int): LocalDate {
    return LocalDate.now().minusDays(numberOfDays.toLong())
}

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

