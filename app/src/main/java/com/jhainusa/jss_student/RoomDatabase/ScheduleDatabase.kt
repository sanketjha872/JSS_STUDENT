package com.jhainusa.jss_student.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Schedule::class], version = 1)
abstract class ScheduleDatabase : RoomDatabase(){
    abstract fun ScheduleDao() : ScheduleDao

    companion object{
        private var INSTANCE : ScheduleDatabase?=null
        fun getDatabase(context : Context) : ScheduleDatabase {
            if(INSTANCE == null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        ScheduleDatabase :: class.java,
                        "schedule_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}