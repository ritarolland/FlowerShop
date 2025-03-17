package com.example.prac1.presentation.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.presentation.viewmodel.ProfileViewModel


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateToFavourites: () -> Unit,
    navigateToOrders: () -> Unit,
    logOut:() -> Unit
) {
    val userInfo by viewModel.userInfo.collectAsState(null)
    val context = LocalContext.current

    viewModel.fetchProfileUrl()

    LaunchedEffect(Unit) {
        viewModel.fetchProfileUrl()
    }

    Scaffold(
        modifier = Modifier.padding(16.dp),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = userInfo?.name ?: "UserName",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                GlideImage(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(256.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    imageUrl = userInfo?.image_url ?: "",
                    description = ""
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToOrders()
                    }
            ) {
                Text(text = "My orders", modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToFavourites()
                    }
            ) {
                Text(text = "Favourites", modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val emailUri = Uri.parse(
                            "mailto:support@example.com?subject=Support%20Request&body=Hello,%20I%20need%20help%20with..."
                        )
                        val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)
                        if (emailIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(emailIntent)
                        }
                    }
            ) {
                Text(text = "Support", modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        logOut()
                        viewModel.logOut()
                    }
            ) {
                Text(text = "Log out", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
        }

    }
}