package com.jhainusa.jss_student.RoomDatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainVIewModel(scheduleRepository) as T
    }
}