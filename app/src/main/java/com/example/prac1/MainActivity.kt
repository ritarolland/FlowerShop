package com.example.prac1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prac1.app.MyApplication
import com.example.prac1.presentation.composable.MainScreen
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel
import com.example.prac1.presentation.viewmodel.AuthViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import com.example.prac1.presentation.viewmodel.FavouritesViewModel
import com.example.prac1.presentation.viewmodel.OrderViewModel
import com.example.prac1.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var catalogViewModel: CatalogViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var allOrdersViewModel: AllOrdersViewModel
    private lateinit var orderViewModel: OrderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        (application as MyApplication).appComponent.inject(this)
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class]
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class]
        catalogViewModel = ViewModelProvider(this, viewModelFactory)[CatalogViewModel::class]
        authViewModel = ViewModelProvider(this, viewModelFactory) [AuthViewModel::class]
        profileViewModel = ViewModelProvider(this, viewModelFactory) [ProfileViewModel::class]
        favouritesViewModel = ViewModelProvider(this, viewModelFactory) [FavouritesViewModel::class]
        allOrdersViewModel = ViewModelProvider(this, viewModelFactory) [AllOrdersViewModel::class]
        orderViewModel = ViewModelProvider(this, viewModelFactory) [OrderViewModel::class]
        setContent {
            MaterialTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        catalogViewModel = catalogViewModel,
                        detailsViewModel = detailsViewModel,
                        cartViewModel = cartViewModel,
                        profileViewModel = profileViewModel,
                        favouritesViewModel = favouritesViewModel,
                        orderViewModel = orderViewModel,
                        allOrdersViewModel = allOrdersViewModel
                    )
                }
            }
        }
    }
}