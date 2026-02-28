package com.jhainusa.jss_student.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Schedule::class, ClassSchedule::class], version = 2)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao
    abstract fun classScheduleDao(): ClassScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        fun getDatabase(context: Context): ScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "schedule_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
