package com.example.prac1.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prac1.app.MyApplication
import com.example.prac1.presentation.navigation.BottomNavigationBar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class]
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class]
        setContent {
            MaterialTheme {
                val catalogViewModel = ViewModelProvider(
                    this,
                    viewModelFactory
                )[CatalogViewModel::class.java]
                navController = rememberNavController()
                BottomNavigationBar(navController, catalogViewModel, detailsViewModel, cartViewModel)
            }
        }
    }
}