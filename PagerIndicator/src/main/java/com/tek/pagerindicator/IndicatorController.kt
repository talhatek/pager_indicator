package com.tek.pagerindicator

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center

class IndicatorController(
    private val count: Int,
    private val size: IntSize,
    private val dotStyle: DotStyle,
    private val orientation: Orientation,
    private val startIndex:Int=0,
    startRange:IntRange = startIndex .. dotStyle.visibleDotCount-1

) {
    private var selectedIndex = mutableStateOf(startIndex)

    internal val colorTargets = SnapshotStateList<Color>()
    internal val colors = mutableListOf<State<Color>>()

    internal val sizeTargets = SnapshotStateList<Float>()
    internal val sizes = mutableListOf<State<Float>>()

    internal val offsetTargets = SnapshotStateList<Offset>()
    internal val offSets = mutableListOf<State<Offset>>()

    private var offsetEach = dotStyle.dotMargin + dotStyle.regularDotRadius.times(2)

    private var visibleRange = startRange

    init {
        for (i in 0 until count) {
            colorTargets.add(if (i == selectedIndex.value) dotStyle.currentDotColor else dotStyle.regularDotColor)
            sizeTargets.add(
                when (i) {
                    selectedIndex.value -> dotStyle.currentDotRadius
                    visibleRange.first -> {
                        if (visibleRange.first != 0)
                            dotStyle.notLastDotRadius
                        else
                            dotStyle.regularDotRadius
                    }
                    visibleRange.last -> {
                        if (visibleRange.last != count - 1)
                            dotStyle.notLastDotRadius
                        else
                            dotStyle.regularDotRadius

                    }
                    in visibleRange -> dotStyle.regularDotRadius
                    else -> 0f
                }
            )

            offsetTargets.add(
                when (orientation) {
                    Orientation.Vertical -> Offset(
                        x = calculateStartOffset() + i.times(dotStyle.dotMargin) + i.times(
                            dotStyle.regularDotRadius.times(2)) - ((startRange.first) * offsetEach),
                        y = size.center.y.toFloat()
                    )
                    else -> Offset(
                        y = calculateStartOffset() + i.times(dotStyle.dotMargin) + i.times(
                            dotStyle.regularDotRadius.times(2)) - ((startRange.first) * offsetEach),
                        x = size.center.x.toFloat()
                    )
                }

            )

        }
    }

    fun pageChanged(index: Int) {
        if (index == selectedIndex.value)
            return
        if (selectedIndex.value > index)
            prev()
        else
            next()
    }

    private fun updateRange(isNext: Boolean) {
        val start = visibleRange.first
        val end = visibleRange.last
        visibleRange = if (isNext) {
            (start + 1)..(end + 1)
        } else {
            (start - 1) .. (end-1)
        }
        Log.e("rangeChanged", "in indicatorController $visibleRange")

    }

    private fun next() {
        if (selectedIndex.value + 1 == visibleRange.last && selectedIndex.value + 1 != count - 1) {
            for (i in 0 until count)
                offsetTargets[i] = when (orientation) {
                    Orientation.Vertical -> Offset(
                        x = offsetTargets[i].x - offsetEach,
                        y = offsetTargets[i].y
                    )
                    else -> Offset(
                        y = offsetTargets[i].y - offsetEach,
                        x = offsetTargets[i].x
                    )
                }

            updateRange(true)
            selectedIndex.value++
            for (i in 0 until count) {
                val size = when (i) {
                    selectedIndex.value -> dotStyle.currentDotRadius
                    visibleRange.first -> {
                        if (visibleRange.first != 0)
                            dotStyle.notLastDotRadius
                        else
                            dotStyle.regularDotRadius
                    }
                    visibleRange.last -> {
                        if (visibleRange.last != count - 1)
                            dotStyle.notLastDotRadius
                        else
                            dotStyle.regularDotRadius

                    }
                    in visibleRange -> dotStyle.regularDotRadius
                    else -> 0f
                }
                val color = when (i) {
                    selectedIndex.value -> dotStyle.currentDotColor
                    else -> dotStyle.regularDotColor
                }
                sizeTargets[i] = size
                colorTargets[i] = color
            }

        } else {
            sizeTargets[selectedIndex.value] = dotStyle.regularDotRadius
            colorTargets[selectedIndex.value] = dotStyle.regularDotColor
            selectedIndex.value++
            sizeTargets[selectedIndex.value] = dotStyle.currentDotRadius
            colorTargets[selectedIndex.value] = dotStyle.currentDotColor
        }


    }

    private fun prev() {
        if (selectedIndex.value - 1 == visibleRange.first && selectedIndex.value - 1 != 0) {
            for (i in 0 until count)
                offsetTargets[i] =
                    when (orientation) {
                        Orientation.Vertical ->
                            Offset(x = offsetTargets[i].x + offsetEach, y = offsetTargets[i].y)
                        else -> Offset(
                            y = offsetTargets[i].y + offsetEach,
                            x = offsetTargets[i].x
                        )
                    }

            updateRange(false)
            selectedIndex.value--
            for (i in 0 until count) {
                val size = when (i) {
                    selectedIndex.value -> dotStyle.currentDotRadius
                    visibleRange.first -> {
                        if (visibleRange.first != 0)
                            dotStyle.notLastDotRadius
                        else
                            dotStyle.regularDotRadius
                    }
                    visibleRange.last -> {
                        if (visibleRange.last != count - 1)
                            dotStyle.notLastDotRadius
                        else
                            dotStyle.regularDotRadius
                    }
                    in visibleRange -> dotStyle.regularDotRadius

                    else -> 0f
                }
                val color = when (i) {
                    selectedIndex.value -> dotStyle.currentDotColor
                    else -> dotStyle.regularDotColor
                }
                sizeTargets[i] = size
                colorTargets[i] = color
            }

        } else {
            sizeTargets[selectedIndex.value] = dotStyle.regularDotRadius
            colorTargets[selectedIndex.value] = dotStyle.regularDotColor
            selectedIndex.value--
            sizeTargets[selectedIndex.value] = dotStyle.currentDotRadius
            colorTargets[selectedIndex.value] = dotStyle.currentDotColor
        }

    }

    private fun calculateStartOffset(): Float {
        var totalDotSize = dotStyle.regularDotRadius.times(2f)

        val till = if (count > dotStyle.visibleDotCount) dotStyle.visibleDotCount else count
        for (i in 1 until till)
            totalDotSize += dotStyle.regularDotRadius.times(2f) + dotStyle.dotMargin

        return when (orientation) {
            Orientation.Vertical -> size.width.div(2f) - totalDotSize.div(2f) + dotStyle.regularDotRadius
            else -> size.height.div(2f) - totalDotSize.div(2f) + dotStyle.regularDotRadius

        }
    }

}

@Composable
fun rememberIndicatorController(
    count: Int,
    size: IntSize,
    dotStyle: DotStyle,
    orientation: Orientation,
    startIndex: Int,
    startRange: IntRange
): IndicatorController {
    return remember{
        IndicatorController(count, size, dotStyle, orientation,startIndex,startRange)
    }
}