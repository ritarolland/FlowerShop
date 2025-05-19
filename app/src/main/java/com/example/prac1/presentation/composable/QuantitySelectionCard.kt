package com.example.prac1.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R

@Composable
fun QuantitySelectionCard(
    quantity: Int,
    modifier: Modifier = Modifier,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(interactionSource = null, indication = null) {
                    if (quantity > 0) {
                        onQuantityChange(quantity - 1)
                    }
                },
            imageVector = ImageVector.vectorResource(R.drawable.minus_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = quantity.toString(),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
        )
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(interactionSource = null, indication = null) {
                    onQuantityChange(quantity + 1)
                },
            imageVector = ImageVector.vectorResource(R.drawable.add_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}