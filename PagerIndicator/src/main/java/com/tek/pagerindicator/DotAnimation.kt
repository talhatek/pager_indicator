package com.tek.pagerindicator


import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DotAnimation(
    val sizeAnim: AnimationSpec<Float> = spring(),
    val offsetAnim: AnimationSpec<Offset> = spring(visibilityThreshold = Offset.VisibilityThreshold),
    val colorAnim: AnimationSpec<Color> = spring(),
)