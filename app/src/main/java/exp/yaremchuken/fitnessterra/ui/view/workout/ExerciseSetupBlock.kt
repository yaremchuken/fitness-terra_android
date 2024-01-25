package exp.yaremchuken.fitnessterra.ui.view.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.element.GifImage
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils

@Composable
fun ExerciseSetupBlock(
    onClick: () -> Unit,
    setup: ExerciseSetup
) {
    val repeatsOrDuration =
        if (setup.sets.isEmpty()) {
            Utils.formatToTime(setup.duration)
        } else if (allRepeatsAreSame(setup.sets)) {
            "${setup.sets.size}x${setup.sets[0]}"
        } else {
            setup.sets.joinToString(",")
        }

    Column(
        Modifier
            .clickable { onClick() }
            .background(Color.White)
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            GifImage(
                Utils.exerciseGifPath(setup.exercise),
                Modifier
                    .height(64.dp)
                    .clip(UIConstants.ROUNDED_CORNER)
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .weight(1F)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = setup.exercise.title,
                        style = AppType.titleMedium
                    )
                    Row {
                        Image(
                            painter = painterResource(
                                id = if (setup.sets.isEmpty()) R.drawable.ic_timer else R.drawable.ic_repeat
                            ),
                            contentDescription = null,
                            Modifier
                                .height(14.dp)
                                .padding(end = 4.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            text = repeatsOrDuration,
                            style = Typography.bodyMedium
                        )
                        if (setup.weight > 0) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_weight),
                                contentDescription = null,
                                Modifier
                                    .height(14.dp)
                                    .padding(start = 12.dp, end = 4.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Text(
                                text = "${setup.weight * .001}",
                                style = Typography.bodyMedium
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { onClick() },
                    Modifier
                        .padding(end = 20.dp)
                        .height(24.dp)
                        .width(24.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward_filled),
                        contentDescription = null)
                }
            }
        }
        Divider(Modifier.padding(top = 20.dp))
    }
}

private fun allRepeatsAreSame(repeats: List<Long>) = repeats.find { it != repeats[0] } == null