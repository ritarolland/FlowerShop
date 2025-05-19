package com.example.prac1.presentation.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.prac1.presentation.navigation.AppNavHost
import com.example.prac1.presentation.navigation.AuthNavHost
import com.example.prac1.presentation.navigation.BottomNavigationBar
import com.example.prac1.presentation.navigation.Screens
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel
import com.example.prac1.presentation.viewmodel.AuthViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import com.example.prac1.presentation.viewmodel.FavouritesViewModel
import com.example.prac1.presentation.viewmodel.OrderViewModel
import com.example.prac1.presentation.viewmodel.ProfileViewModel
import com.example.prac1.presentation.viewmodel.ThemeViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    detailsViewModel: DetailsViewModel,
    cartViewModel: CartViewModel,
    profileViewModel: ProfileViewModel,
    favouritesViewModel: FavouritesViewModel,
    allOrdersViewModel: AllOrdersViewModel,
    orderViewModel: OrderViewModel,
    themeViewModel: ThemeViewModel
) {
    val isAuthorized by authViewModel.isAuthorized.collectAsState()


    LaunchedEffect(isAuthorized) {
        if (isAuthorized == true)
            profileViewModel.fetchProfileUrl()
    }

    if (isAuthorized == false) {
        AuthNavHost(
            navController = navController,
            authViewModel = authViewModel
        )
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController, catalogViewModel = catalogViewModel)
            }
        ) {
            AppNavHost(
                navController = navController,
                catalogViewModel = catalogViewModel,
                detailsViewModel = detailsViewModel,
                cartViewModel = cartViewModel,
                profileViewModel = profileViewModel,
                favouritesViewModel = favouritesViewModel,
                allOrdersViewModel = allOrdersViewModel,
                orderViewModel = orderViewModel,
                themeViewModel = themeViewModel,
                logOut = { authViewModel.logOut() }
            )
        }
    }
}