package exp.yaremchuken.fitnessterra.view.animation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import exp.yaremchuken.fitnessterra.bitmap
import exp.yaremchuken.fitnessterra.bitmaps
import exp.yaremchuken.fitnessterra.model.Exercise

@Composable
fun ExerciseAnimation(
    exercise: Exercise,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val frames = LocalContext.current.bitmaps(exercise).map { i -> i.asImageBitmap() }
    val default = listOf(LocalContext.current.bitmap("exercise", "preview_default")!!.asImageBitmap())
    Animation(
        frames = frames.ifEmpty { default },
        duration = exercise.repeatTime,
        modifier = modifier,
        contentScale = contentScale
    )
}