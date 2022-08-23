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

internal class IndicatorController(
    private val count: Int,
    private val size: IntSize,
    private val dotStyle: DotStyle,
    private val orientation: Orientation,
    private val startIndex: Int = 0,
    startRange: IntRange = startIndex..dotStyle.visibleDotCount.minus(1)

) : IndicatorRangeProcessor, IndicatorMovementProcessor {
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
        Log.e("indicatorController", "init")
        for (i in 0 until count) {
            colorTargets.add(colorFinder(i))
            sizeTargets.add(sizeFinder(i))

            offsetTargets.add(
                when (orientation) {
                    Orientation.Vertical -> Offset(
                        x = calculateStartOffset() + i.times(dotStyle.dotMargin) + i.times(
                            dotStyle.regularDotRadius.times(2)
                        ) - ((startRange.first) * offsetEach),
                        y = size.center.y.toFloat()
                    )
                    else -> Offset(
                        y = calculateStartOffset() + i.times(dotStyle.dotMargin) + i.times(
                            dotStyle.regularDotRadius.times(2)
                        ) - ((startRange.first) * offsetEach),
                        x = size.center.x.toFloat()
                    )
                }

            )

        }
    }

    fun clearAll() {
        sizes.clear()
        offSets.clear()
        colors.clear()
    }

    fun pageChanged(index: Int) {
        Log.e("indicatorController", "pageChanged")

        if (index == selectedIndex.value)
            return
        if (selectedIndex.value > index)
            prev()
        else
            next()
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
            processRangeNext()
            selectedIndex.value++
            for (i in 0 until count) {

                sizeTargets[i] = sizeFinder(i)
                colorTargets[i] = colorFinder(i)
            }

        } else {
            processMovementForward()
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
            processRangePrev()
            selectedIndex.value--
            for (i in 0 until count) {
                sizeTargets[i] = sizeFinder(i)
                colorTargets[i] = colorFinder(i)
            }

        } else {
            processMovementBackward()
        }

    }


    private fun colorFinder(index: Int): Color {
        return when (index) {
            selectedIndex.value -> dotStyle.currentDotColor
            else -> dotStyle.regularDotColor

        }
    }

    private fun sizeFinder(index: Int): Float {
        return when (index) {
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

    override fun processRangeNext() {
        visibleRange = visibleRange.first.plus(1)..visibleRange.last.plus(1)
    }

    override fun processRangePrev() {
        visibleRange = visibleRange.first.minus(1)..visibleRange.last.minus(1)

    }

    override fun processMovementForward() {
        sizeTargets[selectedIndex.value] = dotStyle.regularDotRadius
        colorTargets[selectedIndex.value] = dotStyle.regularDotColor
        selectedIndex.value++
        sizeTargets[selectedIndex.value] = dotStyle.currentDotRadius
        colorTargets[selectedIndex.value] = dotStyle.currentDotColor
    }

    override fun processMovementBackward() {
        sizeTargets[selectedIndex.value] = dotStyle.regularDotRadius
        colorTargets[selectedIndex.value] = dotStyle.regularDotColor
        selectedIndex.value--
        sizeTargets[selectedIndex.value] = dotStyle.currentDotRadius
        colorTargets[selectedIndex.value] = dotStyle.currentDotColor

    }

}

@Composable
internal fun rememberIndicatorController(
    count: Int,
    size: IntSize,
    dotStyle: DotStyle,
    orientation: Orientation,
    startIndex: Int,
    startRange: IntRange
): IndicatorController {
    return remember {
        IndicatorController(count, size, dotStyle, orientation, startIndex, startRange)
    }
}