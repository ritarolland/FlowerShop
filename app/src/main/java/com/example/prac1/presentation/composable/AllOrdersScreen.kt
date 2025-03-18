package com.example.prac1.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllOrdersScreen(allOrdersViewModel: AllOrdersViewModel, onOrderClick:(orderId: String) -> Unit, navigateBack:() -> Unit) {
    val orders by allOrdersViewModel.orders.collectAsState(emptyList())
    LaunchedEffect(Unit) {
        allOrdersViewModel.loadOrders()
    }
    Scaffold(
        containerColor = colorResource(R.color.Neutral20),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = stringResource(R.string.orders),
                        color = colorResource(R.color.Text),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.left_icon),
                        contentDescription = null,
                        tint = colorResource(R.color.Text),
                        modifier = Modifier
                            .clip(
                                CircleShape
                            )
                            .size(42.dp)
                            .clickable {
                                navigateBack()
                            })
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
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

    Column(
        modifier = Modifier.clickable {
            onItemClick()
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.order_from),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = order.created_at.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.right_icon),
                contentDescription = null,
                tint = colorResource(R.color.Text)
            )
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = "₽${order.total_price}",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.padding(6.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = colorResource(R.color.Neutral40)
        )
        Spacer(modifier = Modifier.padding(6.dp))
    }
}