package com.example.prac1.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.prac1.R
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(orderId: String?, orderViewModel: OrderViewModel, navigateBack: () -> Unit, onItemClick:(Flower) -> Unit) {
    val currentOrder by orderViewModel.order.collectAsState(null)
    val orderItemsMap by orderViewModel.flowersMap.collectAsState(emptyMap())
    LaunchedEffect(orderId) {
        if (orderId != null) {
            orderViewModel.loadData(orderId)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            orderViewModel.clearData()
        }
    }
    Scaffold(
        modifier = Modifier.padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = null,
                        modifier = Modifier.clip(CircleShape).clickable { navigateBack() }
                    )
                }
            )
        }
    ) { paddingValues ->
        if (currentOrder == null) {
            CircularProgressIndicator()
        } else {
            val order = currentOrder as OrderDataModel
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text(text = "Order from ${order.created_at}", modifier = Modifier.padding(8.dp))
                Text(text = "Total price: ${order.total_price}", modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.padding(16.dp))
                LazyColumn {
                    items(orderItemsMap.size) { i ->
                        val cartItem = orderItemsMap.keys.toList()[i]
                        val flower = orderItemsMap[cartItem]

                        if (flower == null) {
                            Text(text = "Loading flower for item ${cartItem.id}...")
                        } else {
                            OrderItemCard(
                                flower = flower,
                                cartItem = cartItem
                            ) {
                                onItemClick(flower)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun OrderItemCard(
    flower: Flower,
    cartItem: CartItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                GlideImage(
                    imageUrl = flower.imageUrl,
                    description = flower.name,
                    modifier = Modifier
                        .width(128.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = (flower.price * cartItem.quantity).toString(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        text = flower.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = cartItem.quantity.toString()
                    )
                }
            }
        }
    }
}