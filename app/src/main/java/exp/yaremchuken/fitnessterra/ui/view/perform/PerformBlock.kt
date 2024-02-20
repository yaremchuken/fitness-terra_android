package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.data.model.ExerciseSwitchType
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val TICK = 100.milliseconds

@Composable
fun PerformBlock(
    onFinish: () -> Unit,
    speakOut: (text: String) -> Unit,
    setup: ExerciseSetup,
    setIdx: Int
) {
    val totalRepeats =
        setup.sets[setIdx] * if (setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_REPEAT) 2 else 1

    var stopwatch by remember { mutableStateOf(0.milliseconds) }
    var durationTimer by remember { mutableStateOf(setup.duration) }
    var repeatsCounter by remember { mutableLongStateOf(totalRepeats) }
    var displayCounter by remember { mutableLongStateOf(setup.sets[setIdx]) }

    var speakStarted by remember { mutableStateOf(false) }
    var pause by remember { mutableStateOf(false) }

    if (!speakStarted) {
        if (setup.sets.isNotEmpty()) {
            speakOut("${setup.sets[setIdx]}")
        } else {
            speakOut(stringResource(id = R.string.speak_begin_perform))
        }
        speakStarted = true
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(TICK)
            if (!pause) {
                if (setup.sets.isNotEmpty()) {
                    stopwatch = stopwatch.plus(TICK)
                    val repeatsLeft = totalRepeats - (stopwatch / setup.exercise.performTime).toLong()
                    if (repeatsLeft != repeatsCounter) {
                        repeatsCounter = repeatsLeft
                        if (repeatsCounter != 0L) {
                            displayCounter = repeatsCounter
                            if (setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_REPEAT) {
                                displayCounter = ceil(displayCounter.toDouble()/2).toLong()
                            }
                            speakOut("$displayCounter")
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
        Column(
            Modifier.fillMaxWidth()
        ) {
            if (setup.sets.isNotEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$displayCounter",
                            fontWeight = FontWeight.Bold,
                            style = Typography.headlineLarge,
                            fontSize = 96.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    CircularProgressIndicator(
                        progress = getProgress(stopwatch, setup.exercise.performTime),
                        Modifier
                            .height(200.dp)
                            .width(200.dp)
                            .align(Alignment.Center)
                            .clickable { pause = !pause },
                        strokeWidth = 12.dp,
                        trackColor = Color.LightGray
                    )
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(
                                if (pause) R.drawable.ic_play_filled else R.drawable.ic_pause_filled
                            ),
                            contentDescription = null,
                            Modifier
                                .height(48.dp)
                                .padding(bottom = 20.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = Utils.formatToTime(durationTimer),
                    fontWeight = FontWeight.Bold,
                    style = Typography.headlineLarge,
                    fontSize = 96.sp
                )
            }
            Spacer(Modifier.padding(vertical = 12.dp))
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onFinish() },
                Modifier
                    .width(IntrinsicSize.Min)
                    .border(2.dp, Color.Black, UIConstants.ROUNDED_CORNER),
                shape = UIConstants.ROUNDED_CORNER,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_fast_forward),
                    contentDescription = null,
                    Modifier
                        .height(32.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = stringResource(R.string.skip_btn_title),
                    Modifier.padding(vertical = 4.dp),
                    style = Typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }
    }
}

private fun getProgress(stopwatch: Duration, performTime: Duration) =
    ((stopwatch.inWholeMilliseconds % performTime.inWholeMilliseconds).milliseconds / performTime).toFloat()