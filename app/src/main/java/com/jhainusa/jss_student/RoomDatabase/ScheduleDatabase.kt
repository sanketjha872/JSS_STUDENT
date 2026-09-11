package com.jhainusa.jss_student.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Schedule::class, ClassSchedule::class], version = 4)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao
    abstract fun classScheduleDao(): ClassScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE subject ADD COLUMN roomNo TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): ScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "schedule_database"
                )
                .addMigrations(MIGRATION_3_4)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
