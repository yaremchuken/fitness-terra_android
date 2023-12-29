package exp.yaremchuken.fitnessterra.view.perform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.ExerciseSet
import exp.yaremchuken.fitnessterra.model.WorkoutSection
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import exp.yaremchuken.fitnessterra.view.workout.workoutStub
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun PerformBlock(
    onFinish: () -> Unit = {},
    section: WorkoutSection = workoutStub.sections[0],
    set: ExerciseSet = workoutStub.sections[0].sets[0],
    repeatIdx: Int = 0,
    totalRepeats: Int = 3
) {
    var durationTimer by remember { mutableStateOf(set.duration) }
    var repeatsCounter by remember { mutableLongStateOf(set.repeats[repeatIdx]) }

    var pause by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (!pause) {
            if (set.repeats.isNotEmpty()) {
                delay(set.exercise.repeatTime)
                repeatsCounter--
                if (repeatsCounter == 0L) {
                    onFinish()
                }
            } else {
                delay(1.seconds)
                durationTimer = durationTimer.minus(1.seconds)
                if (durationTimer.inWholeSeconds <= 0) {
                    onFinish()
                }
            }
        }
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ){
        Text(
            text = "${section.title} ${repeatIdx + 1} ${stringResource(R.string.from)} $totalRepeats",
            Modifier.padding(start = 12.dp),
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = set.exercise.title,
            Modifier.padding(start = 12.dp),
            style = Typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (set.repeats.isNotEmpty()) {
                Text(
                    text = "$repeatsCounter",
                    fontWeight = FontWeight.Bold,
                    style = Typography.headlineLarge,
                    fontSize = 120.sp
                )
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