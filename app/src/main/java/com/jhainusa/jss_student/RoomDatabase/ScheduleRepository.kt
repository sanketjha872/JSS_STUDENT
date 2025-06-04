package com.jhainusa.jss_student.RoomDatabase

import androidx.lifecycle.LiveData

class ScheduleRepository(private val scheduleDao: ScheduleDao) {

    fun getSchedule(day : String) : LiveData<List<Schedule>>{
        return scheduleDao.getbyDay(day)
    }
   suspend fun insertSchedule(schedule: Schedule){
        scheduleDao.insert(schedule)
    }

}