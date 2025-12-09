package com.example.tasks.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import com.example.tasks.ui.task.TaskEntryViewModel
import kotlinx.coroutines.launch

data class HomeUiState(val taskList: List<Task> = listOf())

class HomeViewModel(private val repository: TasksRepository) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        viewModelScope.launch {
            repository.getAllTasksStream().collect { tasks ->
                uiState = HomeUiState(tasks)
            }
        }
    }
}