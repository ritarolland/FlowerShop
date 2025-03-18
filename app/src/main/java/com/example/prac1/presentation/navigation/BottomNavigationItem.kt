package com.example.prac1.presentation.navigation

import com.example.prac1.R


data class BottomNavigationItem(
    val label: Int = 0,
    val icon: Int = 0,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = R.string.home,
                icon = R.drawable.home_icon,
                route = Screens.Catalog.route
            ),
            BottomNavigationItem(
                label = R.string.my_cart,
                icon = R.drawable.cart_icon,
                route = Screens.Cart.route
            ),
            BottomNavigationItem(
                label = R.string.my_account,
                icon = R.drawable.user_icon,
                route = Screens.Profile.route
            ),
        )
    }
}
