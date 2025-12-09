package com.example.tasks.data

import android.content.Context

interface AppContainer {
    val tasksRepository: TasksRepository
}

class AppDataContainer(private val context: Context) : com.example.tasks.data.AppContainer {

    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(TasksDatabase.getDatabase(context).taskDao())
    }
}

