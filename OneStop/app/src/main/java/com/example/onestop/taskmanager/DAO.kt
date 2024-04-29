package com.example.onestop.taskmanager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.onestop.reminder.Reminders


@Dao
interface DAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("Delete from to_do")
    suspend fun deleteAll()
    @Query("SELECT * FROM to_do WHERE to_do.id LIKE :objectID")
    suspend fun getObjectUsingID(objectID: Int): Task
    @Query("Select * from to_do")
    suspend fun getTasks():List<Task>
    @get:Query("Select id from to_do")
    val allIds: List<Int?>?

}