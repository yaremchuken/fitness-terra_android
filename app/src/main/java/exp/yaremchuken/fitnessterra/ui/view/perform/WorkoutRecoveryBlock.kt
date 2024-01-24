package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun WorkoutRecoveryBlock(
    onFinish: () -> Unit,
    speakOut: (text: String) -> Unit,
    duration: Duration
) {
    var timer by remember { mutableStateOf(duration) }
    var pause by remember { mutableStateOf(false) }

    if (timer == duration) {
        speakOut(
            stringResource(id = R.string.speak_recovery_time)
                .replace(":seconds", "${duration.inWholeSeconds}")
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            if (!pause) {
                timer = timer.minus(1.seconds)
                if (timer.inWholeSeconds <= 0) {
                    onFinish()
                } else if (timer <= 5.seconds) {
                    speakOut("${timer.inWholeSeconds}")
                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.recovery_title),
            fontWeight = FontWeight.Bold,
            style = Typography.headlineLarge
        )
        Text(
            text = Utils.formatToTime(timer),
            fontWeight = FontWeight.Bold,
            style = Typography.headlineLarge,
            fontSize = 64.sp
        )
        Column(
            Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Button(
                onClick = { onFinish() },
                shape = UIConstants.ROUNDED_CORNER
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fast_forward),
                    contentDescription = null,
                    Modifier
                        .height(32.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = stringResource(R.string.skip_btn_title),
                    Modifier.padding(vertical = 4.dp),
                    style = Typography.headlineMedium
                )
            }
        }
    }
}
