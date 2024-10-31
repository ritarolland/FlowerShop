package com.example.prac1.presentation.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    catalogViewModel: CatalogViewModel,
    detailsViewModel: DetailsViewModel,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Catalog.route,
        modifier = Modifier.padding(paddingValues)
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