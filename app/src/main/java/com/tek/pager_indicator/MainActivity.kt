@file:OptIn(ExperimentalPagerApi::class)

package com.tek.pager_indicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.tek.pagerindicator.DotStyle
import com.tek.pagerindicator.PagerIndicator


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.weight(1f)) {
                        HorizontalPagerIndicator()
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        VerticalPagerIndicator()
                    }

                }
            }
        }
    }
}

@Composable
fun HorizontalPagerIndicator() {
    val pagerState = rememberPagerState()
    Row(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            modifier = Modifier
                .weight(9f),
            count = 7,
            state = pagerState
        ) { page ->
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(
                            red = (0..255).random(),
                            green = (0..255).random(),
                            blue = (0..255).random()
                        )
                    ),
                text = "Page: $page",
                textAlign = TextAlign.Center,
                fontSize = 32.sp
            )
        }
        PagerIndicator(
            modifier = Modifier
                .weight(1f)
                .background(Color.Yellow),
            pagerState = pagerState,
            orientation = Orientation.Horizontal
        )

    }

}

@Composable
fun VerticalPagerIndicator() {
    val pagerState = rememberPagerState()
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier
                .weight(9f),
            count = 11,
            state = pagerState
        ) { page ->
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(
                            red = (0..255).random(),
                            green = (0..255).random(),
                            blue = (0..255).random()
                        )
                    ),
                text = "Page: $page",
                textAlign = TextAlign.Center,
                fontSize = 32.sp
            )
        }
        PagerIndicator(
            modifier = Modifier
                .weight(1f)
                .background(Color.Red),
            pagerState = pagerState,
            dotStyle = DotStyle.defaultDotStyle.copy(
                visibleDotCount = 9,
                currentDotColor = Color.Yellow,
                regularDotColor = Color.White
            )
        )


    }

}

