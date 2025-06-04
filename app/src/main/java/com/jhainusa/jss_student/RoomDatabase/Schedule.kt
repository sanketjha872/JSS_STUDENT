package com.jhainusa.jss_student.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val day : String,
    val subject: String,
    val time : String,
    val teacher : String
)
