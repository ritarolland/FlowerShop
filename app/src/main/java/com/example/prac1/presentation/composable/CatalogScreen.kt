package com.example.prac1.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.CatalogViewModel

@Composable
fun CatalogScreen(catalogViewModel: CatalogViewModel, onItemClick: (Flower) -> Unit) {
    val catalogItems by catalogViewModel.catalogItems.collectAsState(emptyList())
    val searchQuery by catalogViewModel.searchQuery.collectAsState("")
    val favourites by catalogViewModel.favourites.collectAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        containerColor = colorResource(R.color.Neutral20)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(R.color.Neutral10),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.search_icon),
                    tint = colorResource(R.color.Text),
                    contentDescription = null
                )
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { text -> catalogViewModel.updateSearchQuery(text) },
                    textStyle = TextStyle(
                        color = colorResource(R.color.Text),
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(colorResource(R.color.Text)),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.location_icon),
                    tint = colorResource(R.color.Text),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.deliver_to),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(R.color.Text)
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "3517 W. Gray St. Utica, Pennsylvania",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(R.color.Text)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.down_icon),
                    tint = colorResource(R.color.Text),
                    contentDescription = null
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.padding(2.dp)) }
                items(catalogItems.size) { i ->
                    FlowerCard(flower = catalogItems[i],
                        isFavourite = catalogItems[i].id in favourites,
                        onClick = { onItemClick(catalogItems[i]) },
                        onFavourite = { catalogViewModel.toggleIsFavourite(catalogItems[i].id) })
                }
                item { Spacer(modifier = Modifier.padding(8.dp)) }
            }
        }
    }
}

@Composable
fun FlowerCard(flower: Flower, isFavourite: Boolean, onClick: () -> Unit, onFavourite: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.Neutral10)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box {
                GlideImage(
                    imageUrl = flower.imageUrl,
                    description = flower.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                )

            }

            Column {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = flower.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(R.color.Text)
                    )
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(interactionSource = null, indication = null) {
                                onFavourite()
                            },
                        imageVector = if (isFavourite) ImageVector.vectorResource(R.drawable.heart_filled)
                        else ImageVector.vectorResource(R.drawable.heart),
                        contentDescription = null,
                        tint = if (isFavourite) colorResource(R.color.Primary) else colorResource(R.color.Neutral60)
                    )
                }
                Text(
                    text = "₽${flower.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(R.color.Text)
                )
            }


        }
    }
}

