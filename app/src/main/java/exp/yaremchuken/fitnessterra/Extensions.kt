package exp.yaremchuken.fitnessterra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import exp.yaremchuken.fitnessterra.data.model.Exercise
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

const val EXTENSION_PNG = ".png"

const val EXERCISES_FOLDER = "exercise"

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

fun Context.bitmaps(exercise: Exercise): List<Bitmap> {
    val folder = "$EXERCISES_FOLDER/${exercise.id}"
    return try {
        assets
            .list(folder)
            ?.filter { it.startsWith("step_") }
            ?.map {
                BitmapFactory.decodeStream(assets.open("$folder/$it"))
            }
            ?: listOf()
    } catch (e: IOException) {
        throw RuntimeException("Unable to fetch bitmaps for exercise '${exercise.id}'", e)
    }
}

fun LocalDate.toInstant(): Instant = this.atStartOfDay(ZoneId.systemDefault()).toInstant()

fun Instant.toLocalDate(): LocalDate = this.atZone(ZoneId.systemDefault()).toLocalDate()

fun Instant.getHour(): Int = this.atZone(ZoneId.systemDefault()).hour

fun String.uppercaseFirstChar(): String = "${this.substring(0, 1).uppercase()}${this.substring(1)}"
