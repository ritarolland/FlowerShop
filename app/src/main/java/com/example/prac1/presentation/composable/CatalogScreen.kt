package com.example.prac1.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.prac1.R
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(catalogViewModel: CatalogViewModel, onItemClick: (Flower) -> Unit) {
    val catalogItems by catalogViewModel.catalogItems.collectAsState(emptyList())
    val searchQuery by catalogViewModel.searchQuery.collectAsState("")
    val favourites by catalogViewModel.favourites.collectAsState(emptyList())
    Scaffold(modifier = Modifier.padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.catalog))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    catalogViewModel.updateSearchQuery(query)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        catalogViewModel.updateSearchQuery(searchQuery)
                    }
                ),
                placeholder = { Text("Введите запрос") }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = 16.dp)
            ) {
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
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box {
                GlideImage(
                    imageUrl = flower.imageUrl,
                    description = flower.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                )
                IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = {
                    onFavourite()
                }) {
                    Icon(
                        painter = if (isFavourite) painterResource(R.drawable.heart_filled)
                        else painterResource(R.drawable.heart),
                        contentDescription = null
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = flower.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = flower.price.toString())
            }

        }
    }
}

