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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.prac1.R
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.CartViewModel
import java.sql.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel, onItemClick:(Flower) -> Unit) {
    val cartItems by cartViewModel.cartItems.collectAsState(emptyList())
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val totalCost by cartViewModel.totalCost.collectAsState(0.0)
    val isSelectionMode by cartViewModel.isSelectionMode.collectAsState(false)
    val selectedItems by cartViewModel.selectedItems.collectAsState(emptyList())

    Scaffold(
        modifier = Modifier
            .padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(modifier = Modifier.padding(bottom = 4.dp),
                        text = stringResource(id = R.string.cart))
                },
                actions = {
                    Box(modifier = Modifier
                        .then(
                            if (isSelectionMode) {
                                Modifier.padding(end = 4.dp)
                            } else {
                                Modifier.padding(end = 16.dp)
                            }
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            cartViewModel.toggleSelectionMode()
                        }){
                    Text(
                        text = if (isSelectionMode) "Done" else "Select",
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )}
                    if (isSelectionMode) {
                        Box(modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(enabled = selectedItems.isNotEmpty()) {
                                cartViewModel.deleteSelectedCartItems()
                            })
                        {
                            Text(
                                text = "Delete",
                                color = if (selectedItems.isNotEmpty()) Color.Red else Color.Gray,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )}
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Box {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(cartItems.size) { i ->
                    val flower = cartViewModel.getItemById(cartItems[i].flowerId)
                    flower?.let {
                        CartItemCard(
                            flower = it,
                            cartItem = cartItems[i],
                            onClick = { onItemClick(it) },
                            isSelectionMode = isSelectionMode,
                            isChecked = selectedItems.contains(cartItems[i].id),
                            onCheckedChange = {
                                cartViewModel.toggleSelection(cartItems[i].id)
                            },
                            onQuantityChange = { quantity ->
                                cartViewModel.updateCartItemQuantity(cartItems[i].id, quantity)
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.padding(32.dp)) }
            }
            Button(
                onClick = {
                    cartViewModel.placeOrder(Timestamp(System.currentTimeMillis()))
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 64.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(text = "Order, total cost: $totalCost")
            }
        }
    }
}


@Composable
fun CartItemCard(
    flower: Flower,
    cartItem: CartItem,
    isSelectionMode: Boolean,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    onQuantityChange: (Int) -> Unit
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
                    Spacer(modifier = Modifier.padding(8.dp))
                    QuantitySelectionCard(
                        quantity = cartItem.quantity,
                        onQuantityChange = onQuantityChange
                    )
                }
            }
            if (isSelectionMode) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}