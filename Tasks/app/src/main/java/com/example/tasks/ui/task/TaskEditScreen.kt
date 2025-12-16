package com.example.tasks.ui.task

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.TasksTopAppBar
import com.example.tasks.R
import com.example.tasks.ui.AppViewModelProvider
import com.example.tasks.ui.navigation.NavigationDestination
import com.example.tasks.ui.theme.TasksTheme

object TaskEditDestination : NavigationDestination {
    override val route = "task_edit"
    override val titleRes = R.string.edit_task_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val taskUiState = viewModel.taskUiState

    Scaffold(
        topBar = {
            TasksTopAppBar(
                title = stringResource(TaskEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = taskUiState,
            onTaskValueChange = { viewModel.updateUiState(it) },
            onSaveClick = {
                viewModel.updateTask()
                navigateBack() },
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