package com.example.prac1.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.CartViewModel
import java.sql.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onItemClick: (Flower) -> Unit,
    onFavorite: (id: String) -> Unit,
    isFavorite: (id: String) -> Boolean,
) {
    val cartItems by cartViewModel.cartItems.collectAsState(emptyList())
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val totalCost by cartViewModel.totalCost.collectAsState(0.0)
    val isSelectionMode by cartViewModel.isSelectionMode.collectAsState(false)
    val selectedItems by cartViewModel.selectedItems.collectAsState(emptyList())

    Scaffold(
        containerColor = colorResource(R.color.Neutral20),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = stringResource(R.string.my_cart),
                        color = colorResource(R.color.Text),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (isSelectionMode) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(30.dp)
                                .clickable(
                                    enabled = selectedItems.isNotEmpty(),
                                    interactionSource = null,
                                    indication = null
                                ) {
                                    cartViewModel.deleteSelectedCartItems()
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.trash_icon),
                            contentDescription = null,
                            tint = if (selectedItems.isNotEmpty()) colorResource(R.color.Primary)
                            else colorResource(R.color.Neutral60)
                        )
                        CustomButtonOutlined(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .height(30.dp),
                            paddingValues = PaddingValues(vertical = 2.dp, horizontal = 12.dp),
                            onClick = {
                                cartViewModel.toggleSelectionMode()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.done),
                                color = colorResource(R.color.Primary),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        CustomButtonOutlined(
                            modifier = Modifier
                                .height(30.dp)
                                .padding(end = 16.dp),
                            paddingValues = PaddingValues(vertical = 2.dp, horizontal = 12.dp),
                            onClick = {
                                cartViewModel.toggleSelectionMode()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.select),
                                color = colorResource(R.color.Primary),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                            },
                            onFavorite = onFavorite,
                            isFavorite = isFavorite
                        )
                    }
                }
                item { Spacer(modifier = Modifier.padding(32.dp)) }
            }
            CustomButtonFilled(
                onClick = {
                    cartViewModel.placeOrder(Timestamp(System.currentTimeMillis()))
                },
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                enabled = totalCost > 0,
                containerColor = if (totalCost > 0) colorResource(R.color.Primary)
                else colorResource(R.color.Neutral60)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.order),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.Neutral10)
                    )
                    Text(
                        text = "₽$totalCost",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.Neutral10)
                    )
                }
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
    onQuantityChange: (Int) -> Unit,
    onFavorite: (id: String) -> Unit,
    isFavorite: (id: String) -> Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.Neutral10)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlideImage(
                imageUrl = flower.imageUrl,
                description = flower.name,
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Box(modifier = Modifier.height(100.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = flower.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(R.color.Text)
                        )
                        Text(
                            text = "₽${flower.price * cartItem.quantity}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(R.color.Text)
                        )
                    }
                    if (isSelectionMode) {
                        Checkbox(
                            modifier = Modifier.offset(y = (-16).dp, x = 16.dp),
                            checked = isChecked,
                            onCheckedChange = onCheckedChange,
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = colorResource(R.color.Neutral60),
                                checkedColor = colorResource(R.color.Primary),
                            )
                        )
                    }
                }
                var favorite by remember { mutableStateOf(isFavorite(flower.id)) }
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomStart)
                        .offset(y = (-6).dp)
                        .clickable(interactionSource = null, indication = null) {
                            onFavorite(flower.id)
                            favorite = !favorite
                        },
                    imageVector = if (favorite) ImageVector.vectorResource(R.drawable.heart_filled)
                    else ImageVector.vectorResource(R.drawable.heart),
                    contentDescription = null,
                    tint = if (favorite) colorResource(R.color.Primary) else colorResource(R.color.Neutral60)
                )
                QuantitySelectionCard(
                    modifier = Modifier
                        .height(32.dp)
                        .width(105.dp)
                        .align(Alignment.BottomEnd),
                    quantity = cartItem.quantity,
                    onQuantityChange = onQuantityChange
                )
            }
        }
    }
}