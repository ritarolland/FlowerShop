package com.example.prac1.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.DetailsViewModel

@Composable
fun DetailsScreen(flowerId: String?, detailsViewModel: DetailsViewModel) {
    val currentFlower: Flower? by detailsViewModel.selectedItem.collectAsState(Flower())
    val flowerCountInCart: Int? by detailsViewModel.flowerCountInCart.collectAsState(null)
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
    Scaffold { paddingValues ->
        if (currentFlower == null) {
            CircularProgressIndicator()
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
                    if(flowerCountInCart == 0) {
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
                    } else if(flowerCountInCart is Int) {
                        QuantitySelectionCard(flowerCountInCart as Int) { detailsViewModel.changeItemsQuantity() }
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