package com.example.tasks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tasks.ui.home.HomeDestination
import com.example.tasks.ui.home.HomeScreen
import com.example.tasks.ui.task.TaskDetailsDestination
import com.example.tasks.ui.task.TaskDetailsScreen
import com.example.tasks.ui.task.TaskEditDestination
import com.example.tasks.ui.task.TaskEditScreen
import com.example.tasks.ui.task.TaskEntryDestination
import com.example.tasks.ui.task.TaskEntryScreen


@Composable
fun TasksNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) { HomeScreen(
            navigateToItemEntry = { navController.navigate(TaskEntryDestination.route) },
            navigateToItemUpdate = {
        navController.navigate("${TaskDetailsDestination.route}/${it}")
    }
)
}
composable(route = TaskEntryDestination.route) {
TaskEntryScreen(
    navigateBack = { navController.popBackStack() },
    onNavigateUp = { navController.navigateUp() }
)
}
composable(
route = TaskDetailsDestination.routeWithArgs,
arguments = listOf(navArgument(TaskDetailsDestination.taskIdArg) {
    type = NavType.IntType
})
) {
TaskDetailsScreen(
    navigateToEditTask = { navController.navigate("${TaskEditDestination.route}/$it") },
    navigateBack = { navController.navigateUp() }
)
}
composable(
route = TaskEditDestination.routeWithArgs,
arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
    type = NavType.IntType
})
) {
TaskEditScreen(
    navigateBack = { navController.popBackStack() },
    onNavigateUp = { navController.navigateUp() }
)
}
}
}