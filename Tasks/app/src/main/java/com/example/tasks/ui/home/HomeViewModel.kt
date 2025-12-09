package com.example.tasks.ui.home

import androidx.lifecycle.ViewModel
import com.example.tasks.data.Task

class HomeViewModel : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class HomeUiState(val taskList: List<Task> = listOf())