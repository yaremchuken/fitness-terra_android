package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun WorkoutRecoveryBlock(
    onFinish: () -> Unit,
    speakOut: (text: String) -> Unit,
    duration: Duration,
    isSideSwitch: Boolean
) {
    var timer by remember { mutableStateOf(duration) }
    var pause by remember { mutableStateOf(false) }

    val speakThreshold = if (duration >= 30.seconds) 10.seconds else 5.seconds

    if (timer == duration && !isSideSwitch) {
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
                } else if (timer <= speakThreshold) {
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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { pause = !pause },
                Modifier
                    .weight(1F)
                    .border(2.dp, Color.Black, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
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
                    style = Typography.headlineSmall,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = { onFinish() },
                Modifier
                    .weight(1F)
                    .border(2.dp, Color.Black, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
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
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
