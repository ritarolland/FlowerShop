package com.example.prac1.presentation.composable

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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.data.repository.SearchResult
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.CatalogViewModel

@Composable
fun CatalogScreen(catalogViewModel: CatalogViewModel, onItemClick: (Flower) -> Unit) {
    val catalogItems by catalogViewModel.catalogItems.collectAsState(emptyList())
    val searchQuery by catalogViewModel.searchQuery.collectAsState("")
    val searchResult by catalogViewModel.searchResult.collectAsState(SearchResult.Default)
    val favourites by catalogViewModel.favourites.collectAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
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
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.search_icon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = searchQuery,
                    onValueChange = { text -> catalogViewModel.updateSearchQuery(text) },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            innerTextField()
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.enter_query),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                )
                if(searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.trash_icon),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier = Modifier.clickable(interactionSource = null, indication = null) {
                            catalogViewModel.updateSearchQuery("")
                            keyboardController?.hide()
                        }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.location_icon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.deliver_to),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "3517 W. Gray St. Utica, Pennsylvania",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.down_icon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
            }
            when(searchResult) {
                SearchResult.Default -> {
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
                        item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.padding(2.dp)) }
                    }
                }
                SearchResult.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().height(128.dp)) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.error_occurred),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.refresh_icon),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null,
                                modifier = Modifier.clickable(interactionSource = null, indication = null) {
                                    catalogViewModel.loadFoundItems(searchQuery)
                                }.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                SearchResult.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                SearchResult.Success -> {
                    if (catalogItems.isNotEmpty()) {
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
                            item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.padding(2.dp)) }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxWidth().height(128.dp)) {
                            Text(
                                text = stringResource(R.string.nothing_found),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                }
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
            containerColor = MaterialTheme.colorScheme.surface
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
                        color = MaterialTheme.colorScheme.onBackground
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
                        tint = if (isFavourite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "₽${flower.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }


        }
    }
}

