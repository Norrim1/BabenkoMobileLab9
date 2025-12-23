package com.example.tasks.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import com.example.tasks.data.datastore.SortType
import com.example.tasks.data.datastore.TaskSettings
import com.example.tasks.data.datastore.TaskSettingsRepository
import com.example.tasks.ui.task.TaskEntryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(val taskList: List<Task> = listOf(),
                       val sortType: SortType = SortType.DATE_ASC)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: TasksRepository,
    private val settingsRepository: TaskSettingsRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                collectTasks(settings)
            }
        }
    }

    private fun collectTasks(settings: TaskSettings) {
        viewModelScope.launch {
            val baseFlow = settings.selectedDate?.let {
                repository.getTasksByDate(it)
            } ?: repository.getAllTasksStream()

            baseFlow.collect { tasks ->
                uiState = HomeUiState(
                    taskList = applySort(tasks, settings.sortType),
                    sortType = settings.sortType
                )
            }
        }
    }

    private fun applySort(
        tasks: List<Task>,
        sortType: SortType
    ): List<Task> =
        when (sortType) {
            SortType.DATE_ASC -> tasks.sortedBy { it.date }
            SortType.DATE_DESC -> tasks.sortedByDescending { it.date }
            SortType.NAME_ASC -> tasks.sortedBy { it.name }
            SortType.NAME_DESC -> tasks.sortedByDescending { it.name }
        }

    fun onDateSelected(date: LocalDate?) {
        viewModelScope.launch {
            settingsRepository.saveSelectedDate(date)
        }
    }

    fun onSortTypeSelected(sortType: SortType) {
        viewModelScope.launch {
            settingsRepository.saveSortType(sortType)
        }
    }

    fun resetFilter() {
        viewModelScope.launch {
            settingsRepository.saveSelectedDate(null)
            settingsRepository.saveSortType(SortType.DATE_ASC)
        }
    }
}
