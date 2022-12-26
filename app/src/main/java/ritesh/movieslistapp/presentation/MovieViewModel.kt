package ritesh.movieslistapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import ritesh.movieslistapp.data.repository.MovieRepository
import ritesh.movieslistapp.domain.model.Content
import ritesh.movieslistapp.domain.paging.MoviePagingSource
import ritesh.movieslistapp.presentation.SearchWidgetState.CLOSE
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {
    private val pagingData: Flow<PagingData<Content>>

    init {
        pagingData = Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory =
            { MoviePagingSource(repo, _title, _searchTextState.value) }
        ).flow.cachedIn(viewModelScope)
    }

    private val _searchWidgetState: MutableState<SearchWidgetState> = mutableStateOf(CLOSE)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    fun filterData() = if (_searchTextState.value.length >= 3) {
        pagingData.onEach { paging ->
            paging.filter { it.name.contains(_searchTextState.value, true) }
        }
    } else {
        pagingData.onEach { paging ->
            paging.filter { true }
        }
    }
}

