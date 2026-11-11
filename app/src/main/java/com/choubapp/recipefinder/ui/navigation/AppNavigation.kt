package com.choubapp.recipefinder.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.choubapp.recipefinder.ui.detail.DetailScreen
import com.choubapp.recipefinder.ui.favorites.FavoritesScreen
import com.choubapp.recipefinder.ui.home.HomeScreen

private const val HOME = "home"
private const val FAVORITES = "favorites"
private const val DETAIL = "detail"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route
    val showBottomBar = route == HOME || route == FAVORITES

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = HOME,
            modifier = Modifier
                .weight(1f)
                .then(
                    if (showBottomBar) Modifier.consumeWindowInsets(WindowInsets.navigationBars)
                    else Modifier
                )
        ) {
            composable(HOME) {
                HomeScreen(onMealClick = { id -> navController.navigate("$DETAIL/$id") })
            }
            composable(FAVORITES) {
                FavoritesScreen(onMealClick = { id -> navController.navigate("$DETAIL/$id") })
            }
            composable(
                route = "$DETAIL/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { entry ->
                DetailScreen(
                    id = entry.arguments?.getString("id").orEmpty(),
                    onBack = { navController.popBackStack() }
                )
            }
        }

        if (showBottomBar) {
            NavigationBar {
                NavigationBarItem(
                    selected = route == HOME,
                    onClick = { navigateToTab(navController, HOME) },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = route == FAVORITES,
                    onClick = { navigateToTab(navController, FAVORITES) },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    label = { Text("Favorites") }
                )
            }
        }
    }
}

private fun navigateToTab(navController: androidx.navigation.NavController, route: String) {
    navController.navigate(route) {
        popUpTo(HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
