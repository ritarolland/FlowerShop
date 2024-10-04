package com.example.prac1.presentation.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavScreen() {
    Scaffold { paddingValues ->  

    }
}


@Composable
fun HomeScreen() {
    // Ваш контент для главного экрана
    Text(text = "Home")
}

@Composable
fun FavoritesScreen() {
    // Ваш контент для экрана избранного
    Text("Favorites Screen")
}

@Composable
fun ProfileScreen() {
    // Ваш контент для экрана профиля
    Text("Profile Screen")
}

private fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavScreen() {
    BottomNavScreen()
}