package com.example.smartpoultry.utils

import androidx.room.TypeConverter
import java.util.Date


class DbConverters {
    @TypeConverter
    fun fromDate(date : java.sql.Date?) : Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?) : java.sql.Date? {
        return timestamp?.let {it ->
            java.sql.Date(it)
        }
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }


}