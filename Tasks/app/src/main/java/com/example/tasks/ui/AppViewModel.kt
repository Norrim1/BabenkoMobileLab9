package com.example.tasks.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasks.TasksApplication
import com.example.tasks.data.TasksRepository
import com.example.tasks.ui.home.HomeViewModel
import com.example.tasks.ui.task.TaskDetailsViewModel
import com.example.tasks.ui.task.TaskEditViewModel
import com.example.tasks.ui.task.TaskEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle()
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            TaskEntryViewModel(TasksApplication().container.tasksRepository)
        }

        initializer {
            TaskDetailsViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            HomeViewModel()
        }
    }
}

fun CreationExtras.TasksApplication(): TasksApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TasksApplication)