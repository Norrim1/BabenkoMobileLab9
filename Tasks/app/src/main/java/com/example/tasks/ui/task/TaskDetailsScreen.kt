package com.example.tasks.ui.task

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.TasksTopAppBar
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.ui.AppViewModelProvider
import com.example.tasks.ui.navigation.NavigationDestination
import com.example.tasks.ui.theme.TasksTheme

object TaskDetailsDestination : NavigationDestination {
    override val route = "task_details"
    override val titleRes = R.string.task_detail_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navigateToEditTask: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.uiState
    val task = uiState.taskDetails.toTask()

    Scaffold(
        topBar = {
            TasksTopAppBar(
                title = stringResource(TaskDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditTask(task.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_task_title),
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        TaskDetailsBody(
            taskDetailsUiState = uiState,
            onMarkComplete = { viewModel.markComplete() },
            onDelete = { viewModel.deleteTask(navigateBack) },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun TaskDetailsBody(
    taskDetailsUiState: TaskDetailsUiState,
    onMarkComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        TaskDetails(
            task = taskDetailsUiState.taskDetails.toTask(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onMarkComplete,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = true
        ) {
            Text(stringResource(R.string.mark_complete))
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}

@Composable
fun TaskDetails(
    task: Task,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            TaskDetailsRow(
                labelResID = R.string.task_name,
                taskDetail = task.name,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            TaskDetailsRow(
                labelResID = R.string.due_date,
                taskDetail = task.date.toString(),
                modifier = Modifier.padding(
                    horizontal = 15.dp
                )
            )
            TaskDetailsRow(
                labelResID = R.string.description,
                taskDetail = task.description,
                modifier = Modifier.padding(
                    horizontal = 15.dp
                )
            )
        }
    }
}

@Composable
private fun TaskDetailsRow(
    @StringRes labelResID: Int,
    taskDetail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = taskDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_task_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        }
    )
}