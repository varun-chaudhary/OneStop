package com.example.onestop.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reminder")
class Reminders {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var message: String? = null
    var remindDate: Date? = null
}