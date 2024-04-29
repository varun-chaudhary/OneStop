package com.example.onestop.taskmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class],version=1)

abstract class myDatabase : RoomDatabase() {
    abstract fun dao():DAO
    companion object {
        @Volatile
        private var INSTANCE: myDatabase? = null

        fun getInstance(context: Context): myDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    myDatabase::class.java,
                    "To_Do"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
