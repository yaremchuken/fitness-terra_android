package exp.yaremchuken.fitnessterra.util

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.gson.Gson
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.Workout
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

object Utils {

    private const val EXERCISES_FOLDER = "exercise"
    private const val WORKOUT_FOLDER = "workout"
    private const val PREVIEW_DEFAULT = "preview_default"

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
     * Get workout preview, or default preview if specified workout don't have one.
     */
    fun getWorkoutPreview(context: Context, workout: Workout) = (
            context.bitmap(WORKOUT_FOLDER, "${workout.id}") ?:
            context.bitmap(WORKOUT_FOLDER, PREVIEW_DEFAULT)!!
            ).asImageBitmap()

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

    fun exerciseGifPath(exercise: Exercise) = "$EXERCISES_FOLDER/${exercise.id}.gif"

    /**
     * Parse localization strings to find appropriate locale.
     * Default locale is en.
     */
    fun localeFromEntity(localeTokens: String): String {
        val tokens = Gson().fromJson(localeTokens, Array<String>::class.java).toList()
        var token = tokens.firstOrNull { it.startsWith(AppSettings.locale().language) }
        if (token == null) {
            token = tokens.first { it.startsWith("en:") }
        }
        return token.removePrefix("${AppSettings.locale().language}:")
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun flagsToWeekdays(dayFlags: List<Boolean>): List<DayOfWeek> {
        val weekdays = ArrayList<DayOfWeek>()
        dayFlags.forEachIndexed { i, flag ->
            if (flag) {
                weekdays.add(DayOfWeek.of(i + 1))
            }
        }
        return weekdays
    }
}