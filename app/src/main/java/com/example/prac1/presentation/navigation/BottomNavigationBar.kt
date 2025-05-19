package com.example.prac1.presentation.navigation

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.prac1.presentation.viewmodel.CatalogViewModel

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel
) {
    var navigationSelectedItem by remember {
        mutableStateOf(
            BottomNavigationItem().bottomNavigationItems().first()
        )
    }
    CustomBottomNavigation(
        items = BottomNavigationItem().bottomNavigationItems(),
        currentRoute = navigationSelectedItem.route,
    ) { navigationItem ->

        if (navigationSelectedItem.route != navigationItem.route) {
            navigationSelectedItem = navigationItem
        }

        if (navigationItem.route == Screens.Catalog.route) {
            catalogViewModel.updateSearchQuery("")
            //catalogViewModel.loadFoundItems("")
        }
        navController.navigate(navigationItem.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }

    }
}

fun Modifier.shadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

@Composable
fun CustomBottomNavigation(
    items: List<BottomNavigationItem>,
    currentRoute: String?,
    onItemClick: (BottomNavigationItem) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
        .wrapContentHeight()
            .shadow(
                color = MaterialTheme.colorScheme.tertiary,
                borderRadius = 22.dp,
                offsetX = 7.dp,
                offsetY = 7.dp,
                spread = 7.dp,
                blurRadius = 22.dp
            )
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    CustomBottomNavigationItem(
                        item = item,
                        isSelected = currentRoute == item.route,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                onClick = { onItemClick(item) },
                                interactionSource = null,
                                indication = null
                            )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            )
        }
    }

}

@Composable
fun CustomBottomNavigationItem(
    item: BottomNavigationItem,
    isSelected: Boolean,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(item.icon),
            contentDescription = stringResource(item.label),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = stringResource(item.label),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}