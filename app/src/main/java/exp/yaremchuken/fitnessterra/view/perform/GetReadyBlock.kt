package exp.yaremchuken.fitnessterra.view.perform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.Exercise
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils
import exp.yaremchuken.fitnessterra.view.workout.workoutStub
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private val GET_READY_DURATION = 10.seconds

@Preview
@Composable
fun GetReadyBlock(
    onFinish: () -> Unit = {},
    exercise: Exercise = workoutStub.sections[0].sets[0].exercise
) {

    var timer by remember { mutableStateOf(GET_READY_DURATION) }

    LaunchedEffect(Unit) {
        while (timer.inWholeSeconds >= 0) {
            delay(1.seconds)
            timer = timer.minus(1.seconds)
            if (timer.inWholeSeconds <= 0) {
                onFinish()
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.get_ready_title),
            style = Typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = exercise.title,
            style = Typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = Utils.formatToTime(timer),
            fontWeight = FontWeight.Bold,
            style = Typography.headlineLarge,
            fontSize = 64.sp
        )
    }
}
