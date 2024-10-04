package com.example.prac1.presentation.navigation

sealed class Screens(val route : String) {
    object Catalog : Screens("catalog_route")
    object Cart : Screens("cart_route")
    object Profile : Screens("profile_route")
}