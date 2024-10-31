package com.example.prac1.presentation.navigation

sealed class Screens(val route : String) {
    object Auth: Screens("auth_route")
    object Register: Screens("register_route")
    object Catalog : Screens("catalog_route")
    object Cart : Screens("cart_route")
    object Profile : Screens("profile_route")
}