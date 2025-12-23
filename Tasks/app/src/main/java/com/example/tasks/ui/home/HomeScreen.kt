package com.example.tasks.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.TasksTopAppBar
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.data.datastore.SortType
import com.example.tasks.ui.AppViewModelProvider
import com.example.tasks.ui.navigation.NavigationDestination
import com.example.tasks.ui.theme.TasksTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState = viewModel.uiState

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onDateSelected(
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TasksTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            taskList = uiState.taskList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier,
            contentPadding = innerPadding,
            onDateClick = { showDatePicker = true },
            onResetClick = { viewModel.resetFilter() },
            currentSort = uiState.sortType,
            onSortSelected = { viewModel.onSortTypeSelected(it) }
        )
    }
}

@Composable
private fun SortOptions(
    currentSort: SortType,
    onSortSelected: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextButton(
            onClick = { onSortSelected(SortType.DATE_ASC) },
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (currentSort == SortType.DATE_ASC) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Дата восх")
        }
        TextButton(
            onClick = { onSortSelected(SortType.DATE_DESC) },
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (currentSort == SortType.DATE_DESC) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Дата нисх")
        }
        TextButton(
            onClick = { onSortSelected(SortType.NAME_ASC) },
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (currentSort == SortType.NAME_ASC) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Имя восх")
        }
        TextButton(
            onClick = { onSortSelected(SortType.NAME_DESC) },
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (currentSort == SortType.NAME_DESC) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Имя нисх")
        }
    }
}


@Composable
private fun HomeBody(
    taskList: List<Task>,
    onItemClick: (Int) -> Unit,
    onDateClick: () -> Unit,
    onResetClick: () -> Unit,
    currentSort: SortType,
    onSortSelected: (SortType) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            TextButton(
                onClick = onDateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать дату")
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            TextButton(
                onClick = onResetClick,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Сбросить")
            }
        }

        SortOptions(
            currentSort = currentSort,
            onSortSelected = onSortSelected,
            modifier = Modifier.fillMaxWidth()
        )
        if (taskList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет заданий",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = taskList, key = { it.id }) { task ->
                    ToDoTask(
                        task = task,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(task.id) }
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ToDoTask(
    task: Task, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = task.date.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = task.description,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}