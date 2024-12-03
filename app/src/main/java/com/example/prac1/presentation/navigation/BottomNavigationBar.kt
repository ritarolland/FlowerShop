package com.example.prac1.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.prac1.presentation.viewmodel.CatalogViewModel

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel
) {
    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
    NavigationBar {
        BottomNavigationItem().bottomNavigationItems()
            .forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = currentRoute == navigationItem.route,
                    label = {
                        Text(navigationItem.label)
                    },
                    icon = {
                        Icon(
                            navigationItem.icon,
                            contentDescription = navigationItem.label
                        )
                    },
                    onClick = {
                        navigationSelectedItem = index
                        if (navigationItem.route == Screens.Catalog.route) {
                            catalogViewModel.updateSearchQuery("")
                        }
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
    }
}