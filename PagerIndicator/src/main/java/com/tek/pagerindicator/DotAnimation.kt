package com.tek.pagerindicator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DotAnimation(
    val sizeAnim: AnimationSpec<Float>,
    val offsetAnim: AnimationSpec<Offset>,
    val colorAnim: AnimationSpec<Color>
) {
    companion object {
        val defaultDotAnimation = DotAnimation(
            spring(),
            spring(visibilityThreshold = Offset.VisibilityThreshold),
            spring()
        )
    }
}