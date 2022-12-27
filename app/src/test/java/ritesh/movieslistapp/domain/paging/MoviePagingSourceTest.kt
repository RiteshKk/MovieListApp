package ritesh.movieslistapp.domain.paging

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ritesh.movieslistapp.data.remote.MovieApi
import ritesh.movieslistapp.data.remote.dto.ContentDto
import ritesh.movieslistapp.data.remote.dto.ContentItemsDto
import ritesh.movieslistapp.data.remote.dto.MovieDto
import ritesh.movieslistapp.data.remote.dto.PageDto
import ritesh.movieslistapp.data.remote.dto.toPageData
import ritesh.movieslistapp.data.repository.MovieRepository

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviePagingSourceTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val api: MovieApi = mockk(relaxed = true)
    lateinit var movieRepository: MovieRepository
    lateinit var moviePagingSource: MoviePagingSource

    val title: MutableStateFlow<String> = MutableStateFlow("")

    companion object {
        val movieResponse = MovieDto(
            page = PageDto(
                contentItems = ContentItemsDto(
                    content = listOf(
                        ContentDto(
                            name = "The Birds",
                            posterImage = "poster1.jpg"
                        )
                    )
                ),
                pageSize = "20",
                pageNum = "1",
                title = "Romantic Comedy",
                totalContentItems = "54"
            )
        )
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        movieRepository = MovieRepository(api)
        moviePagingSource = MoviePagingSource(repository = movieRepository, title)
    }

    @Test
    fun `movie paging source append - success`() = runTest {
        coEvery { (api.getMovieList(2)) }.returns(movieResponse)
        val expectedResult = PagingSource.LoadResult.Page(
            data = movieResponse.page.toPageData().content,
            prevKey = 1,
            nextKey = 3
        )

        assertEquals(
            expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 2,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
        assertEquals("Romantic Comedy", title.value)
    }

    @Test
    fun `movie paging source refresh - success`() = runTest {
        coEvery { (api.getMovieList(1)) }.returns(movieResponse)
        val expectedResult = PagingSource.LoadResult.Page(
            data = movieResponse.page.toPageData().content,
            prevKey = null,
            nextKey = 2
        )
        assertEquals(
            expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }
}