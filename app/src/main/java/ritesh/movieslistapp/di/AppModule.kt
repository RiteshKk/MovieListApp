package ritesh.movieslistapp.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ritesh.movieslistapp.common.BASE_URL
import ritesh.movieslistapp.data.remote.MockClientInterceptor
import ritesh.movieslistapp.data.remote.MovieApi
import ritesh.movieslistapp.data.repository.MovieRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApi(client: OkHttpClient): MovieApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient(context: Application): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .callTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            ).addInterceptor(
                MockClientInterceptor(context)
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieApi): MovieRepository {
        return MovieRepository(api)
    }
}