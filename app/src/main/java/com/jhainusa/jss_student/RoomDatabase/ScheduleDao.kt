package com.jhainusa.jss_student.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(schedule: Schedule)

    @Query("SELECT * FROM schedule WHERE day = :day ORDER BY time DESC ")
    fun getbyDay(day : String) : LiveData<List<Schedule>>

}