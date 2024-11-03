package com.example.prac1.presentation.composable

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.prac1.presentation.navigation.AppNavHost
import com.example.prac1.presentation.navigation.BottomNavigationBar
import com.example.prac1.presentation.navigation.Screens
import com.example.prac1.presentation.viewmodel.AuthViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    detailsViewModel: DetailsViewModel,
    cartViewModel: CartViewModel
) {
    val isAuthorized by authViewModel.isAuthorized.collectAsState()
    LaunchedEffect(isAuthorized) {
        if (isAuthorized == false) {
            navController.navigate(Screens.Auth.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            paddingValues = paddingValues,
            catalogViewModel = catalogViewModel,
            detailsViewModel = detailsViewModel,
            cartViewModel = cartViewModel
        )
    }
}