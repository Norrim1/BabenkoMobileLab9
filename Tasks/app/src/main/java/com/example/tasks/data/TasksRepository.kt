package com.example.tasks.data

import com.example.tasks.data.datastore.SortType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TasksRepository {

    fun getAllTasksStream(): Flow<List<Task>>

    fun getTaskStream(id: Int): Flow<Task?>

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    fun getTasks(sortType: SortType, date: LocalDate?): Flow<List<Task>>

    fun getTasksByDate(date: LocalDate?): Flow<List<Task>>
}