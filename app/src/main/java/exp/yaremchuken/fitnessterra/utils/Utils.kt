package exp.yaremchuken.fitnessterra.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import exp.yaremchuken.fitnessterra.bitmap

object Utils {

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
    fun getExercisePreview(context: Context, exerciseId: Long) = (
            context.bitmap("exercise/${exerciseId}", "preview") ?:
            context.bitmap("exercise", "preview_default")!!
            ).asImageBitmap()

    /**
     * Represent seconds as time string.
     * 300 secs -> 05:00
     * 4000 secs -> 01:06:40
     */
    fun formatToTime(seconds: Long): String {
        var remainder = seconds
        val hours = (remainder.toFloat() / 3600F).toLong()
        remainder -= hours * 3600
        val minutes = (remainder.toFloat() / 60F).toLong()
        remainder -= minutes * 60

        val hourStr = if (hours == 0L) "" else (hours.toString().padStart(2, '0') + ":")

        return "$hourStr${minutes.toString().padStart(2, '0')}:${remainder.toString().padStart(2, '0')}"
    }
}