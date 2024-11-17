package com.example.prac1.presentation.composable

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.prac1.R

@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    description: String,
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
        }
    )
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}