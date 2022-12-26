package ritesh.movieslistapp.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ritesh.movieslistapp.R
import java.io.IOException
import java.io.InputStream

fun getDrawableByName(
    context: Context,
    name: String
): Int {
    val drawableId = context.resources.getIdentifier(name.dropLast(4), "drawable", context.packageName)
    if (drawableId <= 0) {
        return R.drawable.placeholder_for_missing_posters
    }
    return drawableId
}

fun getBitmapFromAssets(
    context: Context,
    fileName: String = "placeholder_for_missing_posters.png"
): Bitmap? {
    return try {
        val inputStream: InputStream = context.assets.open(fileName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun convertPixelsToDp(
    context: Context,
    pixels: Float
): Float {
    val screenPixelDensity = context.resources.displayMetrics.density
    return (pixels / screenPixelDensity)
}

fun readFileFromAsset(
    context: Context,
    fileName: String
): String {
    return try {
        context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}