package com.example.prac1.presentation.composable

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.presentation.viewmodel.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateToFavourites: () -> Unit,
    navigateToOrders: () -> Unit,
    logOut: () -> Unit
) {
    val userInfo by viewModel.userInfo.collectAsState(null)
    val context = LocalContext.current

    viewModel.fetchProfileUrl()

    LaunchedEffect(Unit) {
        viewModel.fetchProfileUrl()
    }

    Scaffold(
        containerColor = colorResource(R.color.Neutral20),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = stringResource(R.string.my_account),
                        color = colorResource(R.color.Neutral10),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(30.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                logOut()
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.logout_icon),
                        contentDescription = null,
                        tint = colorResource(R.color.Neutral10)
                    )
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GradientBackgroundBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(152.dp)
                    .align(Alignment.TopStart)
            )
            Column(
                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 105.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.Neutral10)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GlideImage(
                            modifier = Modifier
                                .size(62.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape),
                            imageUrl = userInfo?.image_url ?: "",
                            description = ""
                        )
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Veronika",
                                color = colorResource(R.color.Text),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userInfo?.name ?: "example@gmail.com",
                                color = colorResource(R.color.Text),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(R.string.general),
                    color = colorResource(R.color.Text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                ProfileItemCard(
                    text = stringResource(R.string.favorites),
                    imageVector = ImageVector.vectorResource(R.drawable.heart)
                ) {
                    navigateToFavourites()
                }
                ProfileItemCard(
                    text = stringResource(R.string.orders),
                    imageVector = ImageVector.vectorResource(R.drawable.orders_icon)
                ) {
                    navigateToOrders()
                }
                Text(
                    text = stringResource(R.string.help),
                    color = colorResource(R.color.Text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                ProfileItemCard(
                    text = stringResource(R.string.contact_us),
                    imageVector = ImageVector.vectorResource(R.drawable.support_icon)
                ) {
                    val emailUri = Uri.parse(
                        "mailto:support@example.com?subject=Support%20Request&body=Hello,%20I%20need%20help%20with..."
                    )
                    val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)
                    if (emailIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(emailIntent)
                    }
                }
            }

        }
    }
}

@Composable
fun ProfileItemCard(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.Neutral10)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = colorResource(R.color.Text)
            )
            Text(
                modifier = Modifier
                    .height(24.dp)
                    .weight(1f),
                text = text,
                color = colorResource(R.color.Text),
                fontSize = 16.sp
            )
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.right_icon),
                contentDescription = null,
                tint = colorResource(R.color.Text)
            )
        }
    }
}

@Composable
fun GradientBackgroundBox(modifier: Modifier = Modifier) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = modifier
            .onSizeChanged { size = it }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(colorResource(R.color.PrimaryGradientStart), colorResource(R.color.PrimaryGradientEnd)),
                    start = Offset(
                        0.45f * size.width.toFloat(),
                        with(LocalDensity.current) { 16.dp.toPx() }),
                    end = Offset(
                        0.55f * size.width.toFloat(),
                        size.height.toFloat() - with(LocalDensity.current) { 16.dp.toPx() })
                )
            )
    )
}