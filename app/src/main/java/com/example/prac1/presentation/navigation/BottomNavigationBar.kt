package com.example.prac1.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prac1.presentation.composable.CartScreen
import com.example.prac1.presentation.composable.CatalogScreen
import com.example.prac1.presentation.composable.DetailsScreen
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    detailsViewModel: DetailsViewModel,
    cartViewModel: CartViewModel
) {
    var navigationSelectedItem by remember { mutableStateOf(0) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().bottomNavigationItems()
                    .forEachIndexed { index, navigationItem ->
                        NavigationBarItem(
                            selected = index == navigationSelectedItem,
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Catalog.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Screens.Catalog.route) {
                CatalogScreen(catalogViewModel = catalogViewModel) { flower ->
                    navController.navigate("details/${flower.id}")
                }
            }
            composable("details/{flowerId}") { backStackEntry ->
                val flowerId = backStackEntry.arguments?.getString("flowerId")
                DetailsScreen(flowerId = flowerId, detailsViewModel = detailsViewModel)
            }
            composable(Screens.Cart.route) {
                CartScreen(cartViewModel = cartViewModel)
            }
            composable(Screens.Profile.route) {
                //call our composable screens here
            }
        }
    }
}
