package com.jhainusa.jss_student.RoomDatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainVIewModel(private val repository: ScheduleRepository) : ViewModel() {

    fun getAll() = repository.getAllSchedules().asLiveData()

    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch {
            repository.insertSchedule(schedule)
        }
    }

    fun getAttendanceForDate(subjectId: Int, date: String): Flow<ClassSchedule?> {
        return repository.getAttendanceForDate(subjectId, date)
    }

    fun getAttendanceHistory(subjectId: Int) = repository.getAttendanceHistory(subjectId).asLiveData()

    fun updateAttendance(subjectId: Int, date: String, day: String, status: Int) {
        viewModelScope.launch {
            repository.updateAttendance(subjectId, date, day, status)
        }
    }
}
