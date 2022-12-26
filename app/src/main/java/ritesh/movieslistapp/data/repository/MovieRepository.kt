package ritesh.movieslistapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ritesh.movieslistapp.data.remote.MovieApi
import ritesh.movieslistapp.domain.paging.MoviePagingSource
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: MovieApi
) {
    suspend fun getMovieList(page: Int) = api.getMovieList(page)
}