package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private val GET_READY_DURATION = 2.seconds

@Composable
fun GetReadyBlock(
    onFinish: () -> Unit,
    speakOut: (text: String) -> Unit,
    exercise: Exercise
) {

    var timer by remember { mutableStateOf(GET_READY_DURATION) }

    LaunchedEffect(Unit) {
        while (timer.inWholeSeconds >= 0) {
            delay(1.seconds)
            timer = timer.minus(1.seconds)
            if (timer.inWholeSeconds <= 0) {
                onFinish()
            } else if (timer <= 5.seconds) {
                speakOut("${timer.inWholeSeconds}")
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
            Modifier.padding(bottom = 12.dp),
            style = Typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = exercise.title,
            Modifier.padding(bottom = 12.dp),
            style = Typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = Utils.formatToTime(timer),
            fontWeight = FontWeight.Bold,
            style = Typography.headlineLarge,
            fontSize = 64.sp
        )
    }
}
