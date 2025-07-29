package com.example.core.esign_system.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AvatarImage(url: String, size: Dp = 48.dp) {
    GlideImage(
        model = url,
        contentDescription = "Avatar",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}