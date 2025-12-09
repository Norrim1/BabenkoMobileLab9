package com.example.tasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var Instance: TasksDatabase? = null

        fun getDatabase(context: Context): TasksDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TasksDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}