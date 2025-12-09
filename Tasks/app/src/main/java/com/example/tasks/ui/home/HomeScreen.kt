package com.example.tasks.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.tasks.ui.AppViewModelProvider
import com.example.tasks.ui.navigation.NavigationDestination
import com.example.tasks.ui.theme.TasksTheme
import java.time.LocalDate

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
        )
    }
}


@Composable
private fun HomeBody(
    taskList: List<Task>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = "Нет заданий",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(24.dp)
            )
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

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    TasksTheme {
        HomeBody(listOf(
            Task(1, LocalDate.now(), "HELP", "HELP"), Task(2,
                LocalDate.now(), "HELP", "HELP"), Task(3,
                LocalDate.now(), "HELP", "HELP")
        ), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    TasksTheme {
        HomeBody(listOf(), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryItemPreview() {
    TasksTheme {
        ToDoTask(
            Task(1, LocalDate.now(), "HELP", "HELP"),
        )
    }
}
