package ritesh.movieslistapp.common

import android.content.Context
import ritesh.movieslistapp.R
import java.io.IOException

/*fetching drawable from folder by using name
if name not matched with drawable then returning default drawable id
* @param name : drawable name
* @return returning actual drawable id */
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

/*
* converting px to android compatible dp value
* */
fun convertPixelsToDp(
    context: Context,
    pixels: Float
): Float {
    val screenPixelDensity = context.resources.displayMetrics.density
    return (pixels / screenPixelDensity)
}

/*
* reading json file assets folder */
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