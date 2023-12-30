package exp.yaremchuken.fitnessterra.ui.view.animation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import kotlin.time.Duration

@Composable
fun Animation(
    frames: List<ImageBitmap>,
    duration: Duration,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.None
) {
    var currentFrame by remember { mutableIntStateOf(0) }

    val fps = duration.inWholeMilliseconds / frames.size.coerceAtLeast(1)

    LaunchedEffect(Unit) {
        while (true) {
            delay(fps)
            var nextFrame = currentFrame + 1
            if (nextFrame >= frames.size) {
                nextFrame = 0
            }
            currentFrame = nextFrame
        }
    }

    if (frames.isNotEmpty()) {
        Image(
            bitmap = frames[currentFrame],
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}