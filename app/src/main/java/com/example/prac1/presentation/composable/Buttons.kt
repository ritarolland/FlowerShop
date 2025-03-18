package com.example.prac1.presentation.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R

@Composable
fun CustomButtonFilled(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = colorResource(R.color.Primary),
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = colorResource(R.color.Neutral10)
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = enabled
    ) {
        content()
    }
}

@Composable
fun CustomButtonOutlined(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(12.dp),
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = colorResource(R.color.Primary)
            )
            .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp),
        contentPadding = paddingValues,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(R.color.Primary)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        content()
    }
}