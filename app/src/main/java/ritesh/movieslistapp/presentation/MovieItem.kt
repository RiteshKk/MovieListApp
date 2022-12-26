package ritesh.movieslistapp.presentation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ritesh.movieslistapp.common.convertPixelsToDp
import ritesh.movieslistapp.common.getDrawableByName
import ritesh.movieslistapp.domain.model.Content

@Composable
fun MovieItem(
    content: Content
) {
    val dpValue = convertPixelsToDp(LocalContext.current, 30f)
    val topMargin = convertPixelsToDp(LocalContext.current, 36f)
    val bottomMargin = convertPixelsToDp(LocalContext.current, 90f)
    val textTopMargin = convertPixelsToDp(LocalContext.current, 24f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = (dpValue / 2).dp)
            .padding(top = topMargin.dp, bottom = bottomMargin.dp)
    ) {

        Image(
            painter = painterResource(getDrawableByName(LocalContext.current, content.posterImage)),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        val scrollState = rememberScrollState()
        var shouldAnimate by remember {
            mutableStateOf(true)
        }
        LaunchedEffect(key1 = shouldAnimate) {
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(3000, 1000, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
            )
            scrollState.scrollTo(0)
            shouldAnimate = !shouldAnimate
        }
        Text(
            text = content.name,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState, false)
                .padding(top = textTopMargin.dp),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}