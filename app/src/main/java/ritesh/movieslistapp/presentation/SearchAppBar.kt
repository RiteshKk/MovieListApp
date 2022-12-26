package ritesh.movieslistapp.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ritesh.movieslistapp.R.drawable
import ritesh.movieslistapp.presentation.SearchWidgetState.CLOSE
import ritesh.movieslistapp.presentation.SearchWidgetState.OPEN
import ritesh.movieslistapp.presentation.ui.theme.TransparentWhite

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "",
                    color = Color.White
                )
            },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = Color.White,
                    contentDescription = "Search-Cancel",
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    })
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White
            )
        )
    }
}

@Composable
fun DefaultAppBar(
    context: Context,
    title: String,
    onSearchTriggered: () -> Unit
) {

    val gradientBlackWhite = Brush.verticalGradient(
        colors = listOf(
            Color.Black,
            TransparentWhite
        )
    )
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h5
            )
        },
        //  modifier = Modifier.background(gradientBlackWhite),
        backgroundColor = Color.Black,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = { Toast.makeText(context, "back button clicked", Toast.LENGTH_SHORT).show() }) {
                Icon(painter = painterResource(id = drawable.back), contentDescription = "back-button")
            }
        },
        actions = {
            IconButton(onClick = { onSearchTriggered() }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    tint = Color.White,
                    contentDescription = "search-button"
                )
            }
        }
    )
}

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