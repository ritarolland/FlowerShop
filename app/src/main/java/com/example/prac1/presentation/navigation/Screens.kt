package com.example.prac1.presentation.navigation

sealed class Screens(val route : String) {
    object Auth: Screens("auth_route")
    object Register: Screens("register_route")
    object Catalog : Screens("catalog_route")
    object Cart : Screens("cart_route")
    object Profile : Screens("profile_route")
    object Favourites: Screens("favourites_root")
    object AllOrders: Screens("all_orders_root")
    object Order: Screens("order_root/{orderId}") {
        fun orderId(orderId: String) = "order_root/$orderId"
    }
}