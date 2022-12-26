package ritesh.movieslistapp.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import ritesh.movieslistapp.BuildConfig
import ritesh.movieslistapp.common.readFileFromAsset
import java.io.IOException

class MockClientInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Chain): Response {

        return if (BuildConfig.DEBUG) {
            val url = chain.request().url
            val jsonResponse: String = when (url.queryParameter("page")) {
                "1" -> readFileFromAsset(context, "CONTENTLISTINGPAGE-PAGE1.json")
                "2" -> readFileFromAsset(context, "CONTENTLISTINGPAGE-PAGE2.json")
                "3" -> readFileFromAsset(context, "CONTENTLISTINGPAGE-PAGE3.json")
                else -> ""
            }

            Response.Builder()
                .code(200)
                .message(jsonResponse)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(jsonResponse.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            chain.proceed(chain.request())
        }
    }
}