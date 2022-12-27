package ritesh.movieslistapp.presentation.appbar

import android.app.Activity
import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ritesh.movieslistapp.R.drawable

@Composable
fun DefaultAppBar(
    context: Context,
    title: String,
    onSearchTriggered: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                color = Color.White
            )
        },
        //  modifier = Modifier.background(gradientBlackWhite),
        backgroundColor = Color.Black,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = { (context as? Activity)?.finish() }) {
                Icon(
                    painter = painterResource(id = drawable.back),
                    contentDescription = "back-button",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { onSearchTriggered() }) {
                Icon(
                    imageVector = Filled.Search,
                    tint = Color.White,
                    contentDescription = "search-button"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DafaultTopBarPreview() {
    DefaultAppBar(
        context = LocalContext.current,
        title = "Romantic Comedy"
    ) {

    }
}