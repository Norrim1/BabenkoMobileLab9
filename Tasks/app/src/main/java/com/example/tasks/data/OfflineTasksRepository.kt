package com.example.tasks.data

import com.example.tasks.data.datastore.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class OfflineTasksRepository(private val taskDao: TaskDao) : TasksRepository {
    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasks() as Flow<List<Task>>

    override fun getTaskStream(id: Int): Flow<Task?> = taskDao.getTask(id)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)

    override fun getTasks(
        sortType: SortType,
        date: LocalDate?
    ): Flow<List<Task>> {

        val source = when (sortType) {
            SortType.DATE_ASC -> taskDao.tasksByDateAsc()
            SortType.DATE_DESC -> taskDao.tasksByDateDesc()
            SortType.NAME_ASC -> taskDao.tasksByNameAsc()
            SortType.NAME_DESC -> taskDao.tasksByNameDesc()
        }

        return source.map { list ->
            list
                .filter { date == null || it.date == date }
                .map { it }
        }
    }

    override fun getTasksByDate(date: LocalDate?): Flow<List<Task>> = taskDao.getTasksByDate(date)
}