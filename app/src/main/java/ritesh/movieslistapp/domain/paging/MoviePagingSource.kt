package ritesh.movieslistapp.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import ritesh.movieslistapp.data.remote.dto.toPageData
import ritesh.movieslistapp.data.repository.MovieRepository
import ritesh.movieslistapp.domain.model.Content
import java.io.IOException

class MoviePagingSource(
    private val repository: MovieRepository,
    var title: MutableStateFlow<String>,
    val query: String = ""
) : PagingSource<Int, Content>() {
    override fun getRefreshKey(state: PagingState<Int, Content>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.nextKey?.plus(1)
                ?: state.closestPageToPosition(it)?.prevKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> {
        val position = params.key ?: 1
        return try {
            val response = repository.getMovieList(page = position)
            title.emit(response.page.title ?: "")
            val remoteData = response.page.toPageData()
            val content = if (query.length >= 3) {
                remoteData.content.filter { it.name.contains(query, true) }
            } else {
                remoteData.content
            }
            val nextKey = if (remoteData.content.size < params.loadSize) null else position + 1
            val prevKey = if (position == 1) null else position - 1
            LoadResult.Page(
                content,
                prevKey,
                nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}