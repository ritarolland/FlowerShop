package com.example.prac1.presentation.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun DetailsScreen(
    flowerId: String?,
    isFavorite: (id: String) -> Boolean,
    detailsViewModel: DetailsViewModel,
    navigateBack: () -> Unit,
    onFavourite: (id: String) -> Unit
) {
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
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.left_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background,
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
        },
        containerColor = MaterialTheme.colorScheme.background
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
            val scrollState = rememberScrollState()
            val scrollProgress = (scrollState.value / 1000f).coerceIn(0f, 1f)
            Box(modifier = Modifier.fillMaxSize()) {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .align(Alignment.TopCenter),
                    imageUrl = flower.imageUrl,
                    description = flower.name,
                    alpha = 1f - scrollProgress
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .offset(y = (LocalConfiguration.current.screenWidthDp - 24).dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = flower.name.uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "₽${flower.price}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (cartItem == null) {
                            CustomButtonFilled(
                                onClick = {
                                    detailsViewModel.addItemToCart()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(48.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.add_to_cart),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            QuantitySelectionCard(
                                quantity = cartItem!!.quantity,
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(48.dp)
                            ) { quantity ->
                                detailsViewModel.updateCartItemQuantity(
                                    itemId = cartItem!!.id,
                                    newQuantity = quantity
                                )
                            }
                        }

                        var favorite by remember { mutableStateOf(isFavorite(flower.id)) }
                        CustomButtonOutlined(
                            onClick = {
                                onFavourite(flower.id)
                                favorite = !favorite
                            },
                            modifier = Modifier.heightIn(48.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp),
                                imageVector = if (favorite) ImageVector.vectorResource(R.drawable.heart_filled)
                                else ImageVector.vectorResource(R.drawable.heart),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.product_description),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        //text = flower.description,
                        text = "The tiny daisy-like flowers of German chamomile have white collars circling raised, cone-shaped, yellow centers and are less than an inch wide, growing on long, thin, light green stems. Sometimes chamomile grows wild and close to the ground, but you can also find it bordering herb gardens. It can reach up to 3 feet high. German chamomile is native to Europe, north Africa, and some parts of Asia. It is closely related to Roman chamomile (Chamaemelum nobile), which, although less commonly used, has many of the same medicinal properties.The tiny daisy-like flowers of German chamomile have white collars circling raised, cone-shaped, yellow centers and are less than an inch wide, growing on long, thin, light green stems. Sometimes chamomile grows wild and close to the ground, but you can also find it bordering herb gardens. It can reach up to 3 feet high. German chamomile is native to Europe, north Africa, and some parts of Asia. It is closely related to Roman chamomile (Chamaemelum nobile), which, although less commonly used, has many of the same medicinal properties.The tiny daisy-like flowers of German chamomile have white collars circling raised, cone-shaped, yellow centers and are less than an inch wide, growing on long, thin, light green stems. Sometimes chamomile grows wild and close to the ground, but you can also find it bordering herb gardens. It can reach up to 3 feet high. German chamomile is native to Europe, north Africa, and some parts of Asia. It is closely related to Roman chamomile (Chamaemelum nobile), which, although less commonly used, has many of the same medicinal properties.The tiny daisy-like flowers of German chamomile have white collars circling raised, cone-shaped, yellow centers and are less than an inch wide, growing on long, thin, light green stems. Sometimes chamomile grows wild and close to the ground, but you can also find it bordering herb gardens. It can reach up to 3 feet high. German chamomile is native to Europe, north Africa, and some parts of Asia. It is closely related to Roman chamomile (Chamaemelum nobile), which, although less commonly used, has many of the same medicinal properties.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.padding(200.dp))

                }

            }
        }
    }


    /*Scaffold(
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
    }*/
}