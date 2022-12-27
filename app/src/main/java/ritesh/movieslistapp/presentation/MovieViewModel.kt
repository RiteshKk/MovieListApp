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
import kotlinx.coroutines.flow.map
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
    lateinit var pagingDataResult: Flow<PagingData<Content>>

    /* Setting Pager configuration on viewModel initialization */
    init {
        pagingData = Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory =
            { MoviePagingSource(repo, _title) }
        ).flow.cachedIn(viewModelScope)
    }

    /* Handle the state of search view OPEN or CLOSE*/
    private val _searchWidgetState: MutableState<SearchWidgetState> = mutableStateOf(CLOSE)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    /* Handle the search query. This will update the editText text help in searching */
    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    /* Method to update the state on Search view
    * @Param newValue : whether the search view is SearchWidgetState.OPEN or SearchWidgetState.CLOSE */
    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    /** Method to update the search query and text in edit text of search view
     * @param: newValue - updated query string
     **/
    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    /* Holds the title value of the screen
    * it will get title from api
    * */
    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    /*
    * filters the data from Paging data received from api call
    * now UI will observe filterData().
    * based on the _searchTextState it will update the paging data list
    * */
    fun filterData() {
       pagingDataResult = if (_searchTextState.value.length >= 3) {
            pagingData.map { paging ->
                paging.filter { it.name.contains(_searchTextState.value, true) }
            }
        } else {
            pagingData.onEach { paging ->
                paging.filter { true }
            }
        }
    }
}

