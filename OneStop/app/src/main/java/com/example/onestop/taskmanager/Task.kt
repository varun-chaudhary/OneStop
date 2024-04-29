package com.example.onestop.taskmanager

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "To_Do")
class Task {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var title: String? = null
    var priority : String? = null
    var done :Boolean? = null
}