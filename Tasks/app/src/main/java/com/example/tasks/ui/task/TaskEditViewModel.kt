package com.example.tasks.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.launch

class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TasksRepository
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && date.isNotBlank() && description.isNotBlank()
        }
    }

    init {
        viewModelScope.launch {
            repository.getTaskStream(taskId)
                .collect { task ->
                    task?.let {
                        taskUiState = TaskUiState(
                            taskDetails = it.toTaskDetails(),
                            isEntryValid = validateInput(it.toTaskDetails())
                        )
                    }
                }
        }
    }

    fun updateUiState(newDetails: TaskDetails) {
        taskUiState = TaskUiState(
            taskDetails = newDetails,
            isEntryValid = validateInput(newDetails)
        )
    }

    fun updateTask() {
        viewModelScope.launch {
            repository.updateTask(taskUiState.taskDetails.toTask())
        }
    }
}