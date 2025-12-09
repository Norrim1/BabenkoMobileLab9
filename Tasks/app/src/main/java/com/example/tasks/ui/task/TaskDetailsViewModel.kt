package com.example.tasks.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tasks.data.TasksRepository

class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle[TaskDetailsDestination.taskIdArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TaskDetailsUiState(
    val taskDetails: TaskDetails = TaskDetails()
)