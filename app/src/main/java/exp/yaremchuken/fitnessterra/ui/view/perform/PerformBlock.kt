package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val TICK = 100.milliseconds

@Composable
fun PerformBlock(
    onFinish: () -> Unit,
    speakOut: (text: String) -> Unit,
    set: ExerciseSet,
    repeatIdx: Int,
    totalRepeats: Int
) {
    var stopwatch by remember { mutableStateOf(0.milliseconds) }
    var durationTimer by remember { mutableStateOf(set.duration) }
    var repeatsCounter by remember { mutableLongStateOf(set.repeats[repeatIdx]) }

    var speakStarted by remember { mutableStateOf(false) }
    var pause by remember { mutableStateOf(false) }

    if (!speakStarted) {
        speakOut(stringResource(id = R.string.speak_begin_perform))
        speakStarted = true
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(TICK)
            if (!pause) {
                if (set.repeats.isNotEmpty()) {
                    stopwatch = stopwatch.plus(TICK)
                    val repeatsLeft = set.repeats[repeatIdx] - (stopwatch / set.exercise.repeatTime).toLong()
                    if (repeatsLeft != repeatsCounter) {
                        repeatsCounter = repeatsLeft
                        if (repeatsCounter != 0L) {
                            speakOut("$repeatsCounter")
                        }
                    }
                    if (repeatsCounter == 0L) {
                        onFinish()
                    }
                } else {
                    durationTimer = durationTimer.minus(TICK)
                    if (durationTimer.inWholeSeconds <= 0) {
                        onFinish()
                    }
                }
            }
        }
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ){
        Text(
            text = set.exercise.title,
            Modifier.padding(start = 12.dp),
            style = Typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Column(
            Modifier.fillMaxWidth()
        ) {
            if (set.repeats.isNotEmpty()) {
                Text(
                    text = "$repeatsCounter",
                    Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    style = Typography.headlineLarge,
                    fontSize = 120.sp,
                    textAlign = TextAlign.Center
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 38.dp, end = 38.dp, bottom = 24.dp)
                ) {
                    LinearProgressIndicator(
                        progress = getProgress (stopwatch, set.exercise.repeatTime),
                        Modifier
                            .align(Alignment.Center)
                            .height(30.dp)
                    )
                }

            } else {
                Text(
                    text = Utils.formatToTime(durationTimer),
                    fontWeight = FontWeight.Bold,
                    style = Typography.headlineLarge,
                    fontSize = 64.sp
                )
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { pause = !pause },
                shape = UIConstants.ROUNDED_CORNER
            ) {
                Image(
                    painter = painterResource(if (pause) R.drawable.ic_continue else R.drawable.ic_pause),
                    contentDescription = null,
                    Modifier
                        .height(32.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = stringResource(if (pause) R.string.continue_btn_title else R.string.pause_btn_title),
                    Modifier.padding(vertical = 4.dp),
                    style = Typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun getProgress(stopwatch: Duration, repeatTime: Duration) =
    ((stopwatch.inWholeMilliseconds % repeatTime.inWholeMilliseconds).milliseconds / repeatTime).toFloat()