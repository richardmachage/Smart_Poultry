package com.example.smartpoultry.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun myDateFormatter(date : LocalDate) : String{
    return DateTimeFormatter.ofPattern("dd/mm/yyyy").format(date)
}

