package ritesh.movieslistapp.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.compose.collectAsLazyPagingItems
import ritesh.movieslistapp.common.convertPixelsToDp
import ritesh.movieslistapp.presentation.SearchWidgetState.CLOSE
import ritesh.movieslistapp.presentation.SearchWidgetState.OPEN
import ritesh.movieslistapp.presentation.appbar.MainAppBar

@Composable
fun MovieListScreen(
    viewModel: MovieViewModel = hiltViewModel()
) {
    val title = viewModel.title.collectAsState("")

    val searchWidgetState by viewModel.searchWidgetState
    val searchTextState by viewModel.searchTextState

    val context = LocalContext.current
    Scaffold(
        topBar = {
            MainAppBar(
                context = context,
                title = title.value,
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    viewModel.updateSearchTextState(it)
                    viewModel.filterData()
                },
                onCloseClicked = {
                    viewModel.apply {
                        updateSearchTextState("")
                        updateSearchWidgetState(CLOSE)
                    }

                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(OPEN)
                }
            )
        },
        content = { padding ->
            MovieList(padding, viewModel)
        },
    )
}

@Composable
fun MovieList(
    padding: PaddingValues,
    viewModel: MovieViewModel
) {

    viewModel.filterData()
    val pagingData = viewModel.pagingDataResult.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        val horizontalMargin = convertPixelsToDp(LocalContext.current, 15f)

        val configuration = LocalConfiguration.current
        val columnSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 7 else 3
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnSize),
            state = rememberLazyGridState(),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalMargin.dp),
            content = {
                if (pagingData.loadState.refresh is NotLoading) {
                    items(pagingData.itemCount) { index ->
                        pagingData[index]?.let { MovieItem(it) }

                    }
                }
                if (pagingData.loadState.refresh is Error) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Error Occured",
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.caption,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
                if (pagingData.loadState.refresh is Loading) {
                    item {
                        Box(
                            modifier =
                            Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }

                if (pagingData.loadState.append is Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                if (pagingData.loadState.append is Error) {
                    item {
                        ErrorFooter {
                            pagingData.retry()
                        }
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun ErrorFooter(retry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Error occured",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
            )

            Box(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Retry",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            retry.invoke()
                        },
                )
            }
        }
    }
}
