package com.example.smartpoultry.utils

import android.provider.ContactsContract.Data
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.sql.Date
import java.sql.Timestamp

class DbConverters {
    @TypeConverter
    fun fromDate(date : Date?) : Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?) : Date? {
        return timestamp?.let {it ->
            Date(it)
        }
    }
}