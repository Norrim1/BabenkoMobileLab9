package com.example.tasks.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.launch

class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TasksRepository
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle[TaskDetailsDestination.taskIdArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var uiState by mutableStateOf(TaskDetailsUiState())
        private set

    init {
        viewModelScope.launch {
            repository.getTaskStream(taskId).collect { task ->
                task?.let {
                    uiState = TaskDetailsUiState(taskDetails = it.toTaskDetails())
                }
            }
        }
    }

    fun markComplete() {
        viewModelScope.launch {
            uiState.taskDetails.toTask().let { task ->
                repository.updateTask(task.copy(description = task.description + " ✅"))
            }
        }
    }

    fun deleteTask(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteTask(uiState.taskDetails.toTask())
            onDeleted()
        }
    }
}

data class TaskDetailsUiState(
    val taskDetails: TaskDetails = TaskDetails()
)