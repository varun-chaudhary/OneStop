package com.example.onestop.quicknotes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "notes_table")

data class Note(
    //making an id for each text autoGenerate will automatically generate id for each text we don't need to pass
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String?,
    val des: String?,
    var color: Int?
): Serializable

