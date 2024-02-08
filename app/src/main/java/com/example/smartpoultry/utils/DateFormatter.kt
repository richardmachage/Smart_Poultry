package com.example.smartpoultry.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun myDateFormatter( date : LocalDate) : String{
    return DateTimeFormatter.ofPattern("dd MMM yyyy").format(date)
}

