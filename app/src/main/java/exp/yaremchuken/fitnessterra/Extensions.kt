package exp.yaremchuken.fitnessterra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

fun Context.bitmap(fileName: String): Bitmap {
    return try {
        with(assets.open(fileName)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        throw RuntimeException("Unable to load bitmap $fileName")
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