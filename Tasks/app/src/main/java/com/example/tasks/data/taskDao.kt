package com.example.tasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks ORDER BY date")
    fun getAllTasks(): Flow<List<Task?>>

    @Query("SELECT * from tasks WHERE id = :id")
    fun getTask(id: Int): Flow<Task?>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE date = :date")
    fun getTasksByDate(date: LocalDate?): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY date ASC")
    fun tasksByDateAsc(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY date DESC")
    fun tasksByDateDesc(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY name ASC")
    fun tasksByNameAsc(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY name DESC")
    fun tasksByNameDesc(): Flow<List<Task>>
}