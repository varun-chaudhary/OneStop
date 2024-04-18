package com.example.onestop.taskmanager

import androidx.room.Database
import   com.example.onestop.taskmanager.Entity
import androidx.room.RoomDatabase

@Database(entities = [Entity::class],version=1)

abstract class myDatabase : RoomDatabase() {
    abstract fun dao():DAO
}
