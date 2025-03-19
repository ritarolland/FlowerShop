package com.example.prac1.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prac1.presentation.composable.AuthScreen
import com.example.prac1.presentation.composable.RegisterScreen
import com.example.prac1.presentation.viewmodel.AuthViewModel

@Composable
fun AuthNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Auth.route
    ) {
        composable(Screens.Auth.route) {
            AuthScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screens.Register.route)
                }
            )
        }
        composable(Screens.Register.route) {
            RegisterScreen(
                onNavigateToAuth = {
                    navController.popBackStack(Screens.Auth.route, inclusive = false)
                },
                authViewModel = authViewModel
            )
        }
    }
}