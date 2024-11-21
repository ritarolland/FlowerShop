package com.example.prac1.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuantitySelectionCard(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 0.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .clickable {
                        if (quantity > 1) {
                            onQuantityChange(quantity - 1)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "-", fontSize = 24.sp)
            }
            Text(
                text = quantity.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 8.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .clickable {
                        onQuantityChange(quantity + 1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", fontSize = 24.sp)
            }
        }
    }
}