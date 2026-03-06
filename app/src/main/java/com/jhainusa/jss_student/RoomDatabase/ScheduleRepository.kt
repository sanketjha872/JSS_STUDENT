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

    fun getAllSchedulesForDate(date: String): Flow<List<ClassSchedule>> {
        return classScheduleDao.getAllSchedulesForDate(date)
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

        if (status == 0) {
            if (existing != null) {
                if (existing.attendanceStatus == 1) {
                    scheduleDao.decrementTotal(subjectId)
                }
                classScheduleDao.deleteScheduleForDate(subjectId, date)
            }
            return
        }

        if (existing != null) {
            if (existing.attendanceStatus != 1 && status == 1) {
                scheduleDao.incrementTotal(subjectId)
            } else if (existing.attendanceStatus == 1 && status != 1) {
                scheduleDao.decrementTotal(subjectId)
            }
            classScheduleDao.updateSchedule(existing.copy(attendanceStatus = status))
        } else {
            if (status == 1) {
                scheduleDao.incrementTotal(subjectId)
            }
            classScheduleDao.insertOrUpdate(
                ClassSchedule(
                    subjectOwnerId = subjectId,
                    date = date,
                    day = day,
                    attendanceStatus = status
                )
            )
        }
    }

    suspend fun addExtraClass(subjectId: Int, date: String, day: String, timing: String) {
        classScheduleDao.insertOrUpdate(
            ClassSchedule(
                subjectOwnerId = subjectId,
                date = date,
                day = day,
                attendanceStatus = 0,
                timing = timing,
                isExtra = true
            )
        )
    }

    suspend fun updateExtraClassAttendance(classId: Int, status: Int) {
        val existing = classScheduleDao.getClassById(classId)
        if (existing != null) {
            if (existing.attendanceStatus != 1 && status == 1) {
                scheduleDao.incrementTotal(existing.subjectOwnerId)
            } else if (existing.attendanceStatus == 1 && status != 1) {
                scheduleDao.decrementTotal(existing.subjectOwnerId)
            }
            classScheduleDao.updateSchedule(existing.copy(attendanceStatus = status))
        }
    }
}
