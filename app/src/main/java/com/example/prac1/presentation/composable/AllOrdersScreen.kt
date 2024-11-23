package com.example.prac1.presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prac1.R
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllOrdersScreen(allOrdersViewModel: AllOrdersViewModel, onOrderClick:(orderId: String) -> Unit) {
    val orders by allOrdersViewModel.orders.collectAsState(emptyList())
    Scaffold(
        modifier = Modifier.padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My orders") },
                navigationIcon = {
                    Icon(painter = painterResource(R.drawable.arrow_back), contentDescription = null)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(orders.size) { i ->
                Order(orders[i]) { onOrderClick(orders[i].id) }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun Order(order: OrderDataModel, onItemClick:() -> Unit) {
    Card(onClick = onItemClick) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text("Ordered ${order.created_at}")
            Spacer(modifier = Modifier.weight(1f))
            Text("${order.total_price}₽")
        }
    }
}