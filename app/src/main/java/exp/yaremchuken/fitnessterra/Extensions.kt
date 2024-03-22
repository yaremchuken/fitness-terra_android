package exp.yaremchuken.fitnessterra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

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

fun Context.equipment(equipment: EquipmentType?) =
    if (equipment == null) null
    else this.bitmap("equipment", equipment.name)

fun Context.muscle(muscle: MuscleGroupType?) =
    if (muscle == null) null
    else this.bitmap("muscle_group", muscle.name)

fun LocalDate.toInstant(): Instant = this.atStartOfDay(ZoneId.systemDefault()).toInstant()
fun Instant.toLocalDate(): LocalDate = this.atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDateTime.toInstant(): Instant = this.atZone(ZoneId.systemDefault()).toInstant()

fun Instant.getHour(): Int = this.atZone(ZoneId.systemDefault()).hour

fun String.uppercaseFirstChar(): String = "${this.substring(0, 1).uppercase()}${this.substring(1)}"
