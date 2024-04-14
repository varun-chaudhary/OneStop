package com.example.onestop.quicknotes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String?,
    val des: String?,
    var color: Int?
): Serializable

