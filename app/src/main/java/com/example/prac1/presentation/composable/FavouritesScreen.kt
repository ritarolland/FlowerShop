package com.example.prac1.presentation.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prac1.R
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.FavouritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favouritesViewModel: FavouritesViewModel,
    onItemClick: (Flower) -> Unit
) {
    val favouriteFlowers by favouritesViewModel.favouriteFlowers.collectAsState(emptyList())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.favourites))
                },
                navigationIcon = { Icon(painter = painterResource(R.drawable.arrow_back), tint = Color.Black, contentDescription = null ) }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(top = 16.dp)
                .padding(paddingValues)
        ) {
            items(favouriteFlowers.size) { i ->
                FlowerCard(flower = favouriteFlowers[i],
                    isFavourite = true,
                    onClick = { onItemClick(favouriteFlowers[i]) })
            }
            item { Spacer(modifier = Modifier.padding(8.dp)) }
        }
    }
}