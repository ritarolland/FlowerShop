package com.example.prac1.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prac1.app.MyApplication
import com.example.prac1.presentation.navigation.MainScreen
import com.example.prac1.presentation.viewmodel.AuthViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var catalogViewModel: CatalogViewModel
    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class]
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class]
        catalogViewModel = ViewModelProvider(this, viewModelFactory)[CatalogViewModel::class]
        authViewModel = ViewModelProvider(this, viewModelFactory) [AuthViewModel::class]
        setContent {
            MaterialTheme {
                navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    catalogViewModel = catalogViewModel,
                    detailsViewModel = detailsViewModel,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}