package by.gastrofest.android.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import by.gastrofest.android.ui.GastrofestScreen
import by.gastrofest.android.ui.GastrofestUiState
import by.gastrofest.android.ui.screen.GastroSetDetailsScreen

@Composable
fun AppNavHost(state: GastrofestUiState) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            GastrofestScreen(
                state = state,
                onItemClick = { item ->
                    navController.navigate("details/${item.id}")
                }
            )
        }

        composable(
            route = "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")!!
            GastroSetDetailsScreen(id, state)
        }
    }
}
