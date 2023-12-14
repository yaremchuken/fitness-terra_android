package exp.yaremchuken.fitnessterra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

const val EXTENSION_PNG = ".png"

/**
 * Load bitmap from assets folder.
 * It's assumed that images have .png extension by default.
 */
fun Context.bitmap(path: String, filename: String?): Bitmap? {
    return if (filename == null) {
        null
    } else try {
        with(assets.open("$path/$filename$EXTENSION_PNG")) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        throw RuntimeException("Unable to load bitmap $path/$filename$EXTENSION_PNG")
    }
}

fun Context.bitmaps(path: String): Map<String, Bitmap> {
    return try {
        assets.list(path)?.associate {
            Pair(it, BitmapFactory.decodeStream(assets.open("$path/$it")))
        } ?: mapOf()
    } catch (e: IOException) {
        throw RuntimeException("Unable to fetch bitmaps from path '$path'", e)
    }
}