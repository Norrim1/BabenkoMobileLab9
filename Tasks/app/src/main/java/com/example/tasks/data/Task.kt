package com.example.tasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import androidx.room.TypeConverter
import java.time.format.DateTimeFormatter

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val name: String,
    val description: String
)

class Converters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(formatter)

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString, formatter)
}