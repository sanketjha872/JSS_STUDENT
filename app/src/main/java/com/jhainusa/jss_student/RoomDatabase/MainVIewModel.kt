package com.jhainusa.jss_student.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainVIewModel(private val scheduleRepository: ScheduleRepository) : ViewModel(){

    fun getbyDay(day : String) : LiveData<List<Schedule>>{
        return scheduleRepository.getSchedule(day)
    }

    fun insertSchedule(schedule : Schedule){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.insertSchedule(schedule)
        }
    }
    fun delete(){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.deleteAll()
        }
    }
}