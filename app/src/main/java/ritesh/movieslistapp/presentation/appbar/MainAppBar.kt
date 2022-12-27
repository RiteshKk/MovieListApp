package ritesh.movieslistapp.presentation.appbar

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ritesh.movieslistapp.presentation.SearchWidgetState
import ritesh.movieslistapp.presentation.SearchWidgetState.CLOSE
import ritesh.movieslistapp.presentation.SearchWidgetState.OPEN

@Composable
fun MainAppBar(
    context: Context,
    title: String,
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchTriggered: () -> Unit
) {
    when (searchWidgetState) {
        CLOSE -> {
            DefaultAppBar(
                context = context,
                title = title,
                onSearchTriggered = onSearchTriggered
            )
        }
        OPEN -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppBarPreview() {
    MainAppBar(
        context = LocalContext.current,
        title = "Romantic Comedy",
        searchWidgetState = CLOSE,
        searchTextState = "", onTextChange = {},
        onCloseClicked = { },
        onSearchTriggered = {}
    )
}