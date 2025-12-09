package com.example.tasks.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.launch
import java.time.LocalDate


class TaskEntryViewModel(private val repository: TasksRepository) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && date.isNotBlank()
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            repository.insertTask(taskUiState.taskDetails.toTask())
        }
    }
}

data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val name: String = "",
    val date: String = "",
    val description: String = ""
)

fun TaskDetails.toTask(): Task {
    return Task(
        id = this.id,
        name = this.name,
        date = try {
            LocalDate.parse(this.date)
        } catch (e: Exception) {
            LocalDate.now()
        },
        description = this.description
    )
}

fun Task.toTaskDetails(): TaskDetails {
    return TaskDetails(
        id = this.id,
        name = this.name,
        date = this.date.toString(),
        description = this.description
    )
}
