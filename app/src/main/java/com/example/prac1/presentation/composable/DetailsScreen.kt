package com.example.prac1.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(flowerId: String?, detailsViewModel: DetailsViewModel, navigateBack:() -> Unit) {
    val currentFlower: Flower? by detailsViewModel.selectedItem.collectAsState(Flower())
    val cartItem: CartItem? by detailsViewModel.flowerCountInCart.collectAsState(null)
    LaunchedEffect(flowerId) {
        if (flowerId != null) {
            detailsViewModel.loadItemById(flowerId)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            detailsViewModel.clearSelectedItem()
        }
    }
    Scaffold(
        modifier = Modifier.padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Icon(painter = painterResource(R.drawable.arrow_back), contentDescription = null, modifier = Modifier.clip(
                        CircleShape).clickable {
                        navigateBack()
                    })
                }
            )
        }
    ) { paddingValues ->
        if (currentFlower == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val flower = currentFlower as Flower
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Text(
                        text = flower.name,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 24.sp
                    )

                    GlideImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        imageUrl = flower.imageUrl,
                        description = flower.name
                    )


                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = flower.price.toString(),
                        fontSize = 24.sp
                    )
                    if(cartItem == null) {
                        Button(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            onClick = {
                                //start showing progress bar
                                detailsViewModel.addItemToCart()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.add_to_cart))
                        }
                    } else {
                        QuantitySelectionCard(cartItem!!.quantity) { quantity ->
                            detailsViewModel.updateCartItemQuantity(
                            itemId = cartItem!!.id,
                            newQuantity = quantity
                        ) }
                    }

                    Text(
                        text = flower.description,
                        modifier = Modifier.padding(8.dp)
                    )

                }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}