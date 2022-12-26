package ritesh.movieslistapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import androidx.paging.flatMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ritesh.movieslistapp.data.repository.MovieRepository
import ritesh.movieslistapp.domain.model.Content
import ritesh.movieslistapp.domain.paging.MoviePagingSource
import ritesh.movieslistapp.presentation.SearchWidgetState.CLOSE
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

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

    val pagingData = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory =
        { MoviePagingSource(repo, _title,_searchTextState.value) }
    ).flow.cachedIn(viewModelScope)

    fun filterData() {
       val result = if (_searchTextState.value.length >= 3) {
            pagingData.onEach { paging ->
                paging.filter { it.name.contains(_searchTextState.value, true) }
            }
        } else {
            pagingData.onEach { paging ->
                paging.filter { true }
            }
        }
    }
}