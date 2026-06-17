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

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch {
            repository.deleteSchedule(schedule)
        }
    }

    fun getAttendanceForDate(subjectId: Int, date: String): Flow<ClassSchedule?> {
        return repository.getAttendanceForDate(subjectId, date)
    }

    fun getAllSchedulesForDate(date: String) = repository.getAllSchedulesForDate(date).asLiveData()

    fun getAttendanceHistory(subjectId: Int) = repository.getAttendanceHistory(subjectId).asLiveData()

    fun getAttendanceInRange(subjectId: Int, startDate: String, endDate: String) = 
        repository.getAttendanceInRange(subjectId, startDate, endDate).asLiveData()

    fun observeSchedule(subjectId: Int) = repository.observeSchedule(subjectId).asLiveData()

    fun getAllAttendance() = repository.getAllAttendanceRecords().asLiveData()

    fun updateAttendance(subjectId: Int, date: String, day: String, status: Int) {
        viewModelScope.launch {
            repository.updateAttendance(subjectId, date, day, status)
        }
    }

    fun addExtraClass(subjectId: Int, date: String, day: String, timing: String) {
        viewModelScope.launch {
            repository.addExtraClass(subjectId, date, day, timing)
        }
    }

    fun updateExtraClassAttendance(classId: Int, status: Int) {
        viewModelScope.launch {
            repository.updateExtraClassAttendance(classId, status)
        }
    }
}
