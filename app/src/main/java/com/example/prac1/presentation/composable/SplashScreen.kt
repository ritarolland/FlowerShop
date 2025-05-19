package com.example.prac1.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onLoadingComplete: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        GradientBackgroundBox(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = ImageVector.vectorResource(R.drawable.tulip_icon),
                contentDescription = null,
                tint = colorResource(R.color.Neutral10)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.flowery),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(R.color.Neutral10),
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        onLoadingComplete()
    }
}



