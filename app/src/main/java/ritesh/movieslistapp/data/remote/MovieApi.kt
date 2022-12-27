package ritesh.movieslistapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ritesh.movieslistapp.data.remote.dto.MovieDto

/* Api to fetch data from server or mock server */
interface MovieApi {
    @GET("movies")
    suspend fun getMovieList(
        @Query("page") page: Int
    ): MovieDto
}