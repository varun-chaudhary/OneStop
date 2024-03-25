package com.example.onestop.reminder

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update



@Dao
interface RoomDAO {

    @TypeConverter
    @Insert
    suspend fun Insert(vararg reminders: Reminders)

    @Update
    suspend fun Update(vararg reminders: Reminders)

    @Delete
    suspend fun Delete(reminders: Reminders)

    @Query("Select * from reminder order by remindDate")
    suspend fun orderThetable():List<Reminders>

    @get:Query("Select * from reminder Limit 1")
    val recentEnteredData: Reminders

    @get:Query("Select * from reminder")
    val all: List<Reminders?>

    @Query("Delete from reminder")
    suspend fun DeleteAll()

    @Query("SELECT * FROM reminder WHERE reminder.id LIKE :objectID")
    suspend fun getObjectUsingID(objectID: Int): Reminders

    @get:Query("SELECT id FROM REMINDER")
    val allIds: List<Int?>?
}