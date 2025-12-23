package com.example.tasks.data

import android.content.Context
import com.example.tasks.data.datastore.TaskSettingsRepository

interface AppContainer {
    val tasksRepository: TasksRepository
    val settingsRepository: TaskSettingsRepository
}

class AppDataContainer(private val context: Context) : com.example.tasks.data.AppContainer {

    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(TasksDatabase.getDatabase(context).taskDao())
    }

    override val settingsRepository: TaskSettingsRepository by lazy {
        TaskSettingsRepository(context)
    }
}

