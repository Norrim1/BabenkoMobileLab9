package com.example.tasks

import android.app.Application
import com.example.tasks.data.AppContainer
import com.example.tasks.data.AppDataContainer

class TasksApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}