package by.gastrofest.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageCarousel(images: List<String>) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { images.size }
    )

    var zooming by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(pagerState),
            userScrollEnabled = !zooming,
            modifier = Modifier
                .height(260.dp)
                .fillMaxWidth()
        ) { page ->
            ZoomableImage(
                url = images[page],
                onZoomChanged = { zoom -> zooming = zoom }
            )
        }

        PagerIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(12.dp)
        )
    }
}
