package com.example.smartpoultry.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun myDateFormatter(date : LocalDate) : String{
    return DateTimeFormatter.ofPattern("dd/mm/yyyy").format(date)
}


@RequiresApi(Build.VERSION_CODES.O)
fun localDateToJavaDate(localDate: LocalDate): Long{
    val startOfDayUtc = localDate.atStartOfDay(ZoneId.of("UTC"))

    return startOfDayUtc.toInstant().toEpochMilli()
}

@SuppressLint("SimpleDateFormat")
fun toGraphDate(date: Date) : String{
    return SimpleDateFormat("dd-MMM").format(date)
}
