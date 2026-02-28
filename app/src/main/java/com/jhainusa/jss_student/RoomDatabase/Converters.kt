package com.jhainusa.jss_student.RoomDatabase
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhainusa.jss_student.RoomDatabase.DaySchedule

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromDayScheduleList(value: String?): List<DaySchedule> {
        if (value.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<DaySchedule>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toDayScheduleList(list: List<DaySchedule>?): String {
        return gson.toJson(list)
    }
}
