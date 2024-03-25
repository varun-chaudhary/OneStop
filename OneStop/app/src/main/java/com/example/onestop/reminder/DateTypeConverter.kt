package com.example.onestop.reminder

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {
    @TypeConverter
    fun LongtoDateConverter(date: Long?): Date {
        return Date(date!!)
    }

    @TypeConverter
    fun DatetoLongConverter(date: Date): Long {
        return date.time
    }
}