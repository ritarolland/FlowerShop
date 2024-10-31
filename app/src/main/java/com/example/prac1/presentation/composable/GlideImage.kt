package com.example.prac1.presentation.composable

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.prac1.R

@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    description: String
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = { ImageView(context).apply {
            contentDescription = description
        } },
        update = { imageView ->
            Glide.with(context)
                .load(imageUrl)
                .placeholder(ColorDrawable(Color.GRAY))
                .error(R.drawable.fairy)
                .into(imageView)
        }
    )
}