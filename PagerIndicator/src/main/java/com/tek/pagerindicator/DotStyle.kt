package com.tek.pagerindicator

import androidx.compose.ui.graphics.Color


data class DotStyle(
    val currentDotRadius: Float,
    val notLastDotRadius: Float,
    val regularDotRadius: Float,
    val dotMargin: Float,
    val visibleDotCount: Int,
    val currentDotColor: Color,
    val regularDotColor: Color
) {
    companion object {
       private const val defaultVisibleDotCount = 5
       private const val defaultRegularRadius = 4f
       private const val defaultDotNotLastRadius = 2f
       private const val defaultCurrentDotRadius = 8f
       private const val defaultDotMargin = defaultRegularRadius.times(3f)
       private val defaultCurrentDotColor = Color(0xFF0d6efd)
       private val defaultRegularDotColor = Color(0xFF6c757d)
        val defaultDotStyle = DotStyle(
            defaultCurrentDotRadius,
            defaultDotNotLastRadius,
            defaultRegularRadius,
            defaultDotMargin,
            defaultVisibleDotCount,
            defaultCurrentDotColor,
            defaultRegularDotColor
        )
    }
}