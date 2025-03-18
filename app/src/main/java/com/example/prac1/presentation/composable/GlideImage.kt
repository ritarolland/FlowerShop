package com.example.prac1.presentation.composable

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.prac1.R
import kotlinx.coroutines.delay


@Composable
fun CoilImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    description: String,
) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(context.isNetworkAvailable()) } // Следим за сетью
    var forceUpdate by remember { mutableStateOf(false) } // Флаг обновления

    // Запускаем проверку сети в фоновом режиме
    LaunchedEffect(Unit) {
        while (true) {
            val newConnectionState = context.isNetworkAvailable()
            if (!isConnected && newConnectionState) {
                forceUpdate = !forceUpdate // Переключаем флаг, чтобы пересоздать `ImageRequest`
            }
            isConnected = newConnectionState
            delay(5000) // Проверяем сеть каждые 5 секунд
        }
    }

    AsyncImage(
        model = imageUrl?.let { if (isConnected) it else R.drawable.fairy } ?: R.drawable.fairy,
        contentDescription = description,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
}




@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    description: String,
    alpha: Float = 1f
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ImageView(context).apply {
            contentDescription = description
        } },
        update = { imageView ->
            val isConnected = context.isNetworkAvailable()
            Glide.with(context)
                .load(if (isConnected && imageUrl != null) imageUrl else R.drawable.fairy)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(ColorDrawable(Color.GRAY))
                .error(R.drawable.fairy)
                .into(imageView)
            imageView.alpha = alpha
        }
    )
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}