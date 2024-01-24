package exp.yaremchuken.fitnessterra.util

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.Workout
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

object Utils {

    const val EXERCISES_FOLDER = "exercise"
    const val PREVIEW_DEFAULT = "preview_default"

    val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withZone(ZoneId.systemDefault())
    val DATE_SHORT_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM").withZone(ZoneId.systemDefault())
    val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:ss").withZone(ZoneId.systemDefault())

    /**
     * Get tint for this image background.
     * Takes four pixels from image sides to get average pixel Color.
     */
    fun getBackgroundTint(bitmap: ImageBitmap): Color {
        val buffer = IntArray(bitmap.height * bitmap.width)
        bitmap.readPixels(buffer)

        return Color(
            buffer[0] +
            buffer[bitmap.width] +
            buffer[bitmap.height * bitmap.width - 1] +
            buffer[bitmap.height * bitmap.width - 1 - bitmap.width]
        )
    }

    /**
     * Get preview for current exercise, if it not exists then default preview.
     */
    fun getExercisePreview(context: Context, exercise: Exercise) = (
            context.bitmap(EXERCISES_FOLDER, "${exercise.id}_0") ?:
            context.bitmap(EXERCISES_FOLDER, PREVIEW_DEFAULT)!!
            ).asImageBitmap()

    /**
     * For simplicity lets just take preview of first exercise of this workout.
     */
    fun getWorkoutPreview(context: Context, workout: Workout) =
        getExercisePreview(context, workout.sections[0].setups[0].exercise)

    /**
     * Represent seconds as time string.
     * 300 secs -> 05:00
     * 4000 secs -> 01:06:40
     */
    fun formatToTime(duration: Duration): String {
        var remainder = duration.inWholeSeconds
        val hours = (remainder.toFloat() / 3600F).toLong()
        remainder -= hours * 3600
        val minutes = (remainder.toFloat() / 60F).toLong()
        remainder -= minutes * 60

        val hourStr = if (hours == 0L) "" else (hours.toString().padStart(2, '0') + ":")

        return "$hourStr${minutes.toString().padStart(2, '0')}:${remainder.toString().padStart(2, '0')}"
    }
}