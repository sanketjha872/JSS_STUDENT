package com.jhainusa.jss_student.RoomDatabase

import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val classScheduleDao: ClassScheduleDao
) {
    fun getAllSchedules(): Flow<List<Schedule>> = scheduleDao.getAllSchedules()

    suspend fun insertSchedule(schedule: Schedule) {
        scheduleDao.insertSchedule(schedule)
    }

    fun observeSchedule(id: Int): Flow<Schedule> = scheduleDao.observeSchedule(id)

    fun getAttendanceForDate(subjectId: Int, date: String): Flow<ClassSchedule?> {
        return classScheduleDao.getScheduleForDate(subjectId, date)
    }

    fun getAttendanceHistory(subjectId: Int): Flow<List<ClassSchedule>> {
        return classScheduleDao.getAttendanceHistory(subjectId)
    }

    fun getAllAttendanceRecords(): Flow<List<ClassSchedule>> {
        return classScheduleDao.getAllAttendanceRecords()
    }

    suspend fun deleteSchedule(schedule: Schedule) {
        scheduleDao.deleteSubject(schedule)
    }

    suspend fun updateAttendance(subjectId: Int, date: String, day: String, status: Int) {
        val existing = classScheduleDao.getScheduleForDateSync(subjectId, date)
        if (existing != null) {
            classScheduleDao.updateSchedule(existing.copy(attendanceStatus = status))
        } else {
            classScheduleDao.insertOrUpdate(
                ClassSchedule(
                    subjectOwnerId = subjectId,
                    date = date,
                    day = day,
                    attendanceStatus = status
                )
            )
        }
        
        // Update total classes count if needed
        if (status == 1) {
            scheduleDao.incrementTotal(subjectId)
        } else if (existing?.attendanceStatus == 1) {
            scheduleDao.decrementTotal(subjectId)
        }
    }
}
