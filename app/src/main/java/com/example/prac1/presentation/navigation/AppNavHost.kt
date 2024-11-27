package com.example.prac1.presentation.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prac1.presentation.composable.AllOrdersScreen
import com.example.prac1.presentation.composable.CartScreen
import com.example.prac1.presentation.composable.CatalogScreen
import com.example.prac1.presentation.composable.DetailsScreen
import com.example.prac1.presentation.composable.FavouritesScreen
import com.example.prac1.presentation.composable.OrderScreen
import com.example.prac1.presentation.composable.ProfileScreen
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import com.example.prac1.presentation.viewmodel.FavouritesViewModel
import com.example.prac1.presentation.viewmodel.OrderViewModel
import com.example.prac1.presentation.viewmodel.ProfileViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    detailsViewModel: DetailsViewModel,
    cartViewModel: CartViewModel,
    profileViewModel: ProfileViewModel,
    favouritesViewModel: FavouritesViewModel,
    allOrdersViewModel: AllOrdersViewModel,
    orderViewModel: OrderViewModel,
    logOut:() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Catalog.route,
        modifier = Modifier.padding(PaddingValues(bottom = 56.dp))
    ) {
        composable(Screens.Catalog.route) {
            CatalogScreen(catalogViewModel = catalogViewModel) { flower ->
                navController.navigate("details/${flower.id}")
            }
        }
        composable(Screens.Favourites.route) {
            FavouritesScreen(favouritesViewModel = favouritesViewModel, onItemClick =  { flower ->
                navController.navigate("details/${flower.id}")
            }, navigateBack = {navController.popBackStack()})
        }
        composable(Screens.AllOrders.route) {
            AllOrdersScreen(allOrdersViewModel = allOrdersViewModel, onOrderClick = { orderId ->
                navController.navigate(Screens.Order.orderId(orderId))
            }, navigateBack = { navController.popBackStack() })
        }
        composable(Screens.Order.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            OrderScreen(orderId = orderId, orderViewModel = orderViewModel, navigateBack = {
                navController.popBackStack()
            }, onItemClick = { flower ->
                navController.navigate("details/${flower.id}")
            })
        }
        composable("details/{flowerId}") { backStackEntry ->
            val flowerId = backStackEntry.arguments?.getString("flowerId")
            DetailsScreen(flowerId = flowerId, detailsViewModel = detailsViewModel, navigateBack = {navController.popBackStack()})
        }
        composable(Screens.Cart.route) {
            CartScreen(cartViewModel = cartViewModel) { flower ->
                navController.navigate("details/${flower.id}")
            }
        }
        composable(Screens.Profile.route) {
            ProfileScreen(viewModel = profileViewModel,
                navigateToOrders = { navController.navigate(Screens.AllOrders.route) },
                navigateToFavourites = { navController.navigate(Screens.Favourites.route) },
                logOut = logOut)
        }
    }
}