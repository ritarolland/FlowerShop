package com.example.prac1.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.domain.model.Flower

@Composable
fun CatalogScreen(catalogItems: List<Flower>, onItemClick: (Flower) -> Unit) {
    Scaffold(modifier = Modifier.padding(16.dp)) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.catalog),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(catalogItems.size) { i ->
                    FlowerCard(flower = catalogItems[i], onClick = { onItemClick(catalogItems[i]) })
                }
            }
        }
    }
}

@Composable
fun FlowerCard(flower: Flower, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(id = R.drawable.rose),
                contentDescription = flower.name
            )
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

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    val products = listOf(
        Flower(id = "1", price = 20.5, name = "Rose", description = "blahblah"),
        Flower(id = "2", price = 20.5, name = "Rose", description = "blahblah"),
        Flower(id = "3", price = 20.5, name = "Rose", description = "blahblah"),
        Flower(id = "4", price = 20.5, name = "Rose", description = "blahblah"),
        Flower(id = "5", price = 20.5, name = "Rose", description = "blahblah")
    )

    CatalogScreen(catalogItems = products) { product ->
        // Обработка клика по продукту (в этом предварительном просмотре можно не реализовывать)
    }
}