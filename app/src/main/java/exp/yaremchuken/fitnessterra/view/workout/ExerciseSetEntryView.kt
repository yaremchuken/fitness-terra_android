package exp.yaremchuken.fitnessterra.view.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.model.ExerciseSet
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.utils.Utils

@Preview
@Composable
fun ExerciseSetEntryView(
    onClick: () -> Unit = {},
    exerciseSet: ExerciseSet = workoutStub.sections[0].sets[0]
) {
    val preview = Utils.getExercisePreview(LocalContext.current, exerciseSet.exercise)

    val repeatsOrDuration =
        if (exerciseSet.repeats.isEmpty()) {
            if (exerciseSet.durations.sorted()[0] == exerciseSet.durations.sorted()[exerciseSet.durations.size-1]) {
                "${exerciseSet.durations.size}x${Utils.formatToTime(exerciseSet.durations[0])}"
            } else {
                exerciseSet.durations.map { Utils.formatToTime(it) }.joinToString { ", " }
            }
        } else if (exerciseSet.repeats.sorted()[0] == exerciseSet.repeats.sorted()[exerciseSet.repeats.size-1]) {
            "${exerciseSet.repeats.size}x${exerciseSet.repeats[0]}"
        } else {
            exerciseSet.repeats.joinToString(",")
        }

    Column(
        Modifier
            .background(Color.White)
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = preview,
                contentDescription = null,
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
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = exerciseSet.exercise.title,
                        style = Typography.titleMedium
                    )
                    Row {
                        Image(
                            painter = painterResource(
                                id = if (exerciseSet.repeats.isEmpty()) R.drawable.ic_timer else R.drawable.ic_repeat
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
                        if (exerciseSet.weight > 0) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_weight),
                                contentDescription = null,
                                Modifier
                                    .height(14.dp)
                                    .padding(start = 12.dp, end = 4.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Text(
                                text = "${exerciseSet.weight * .01}",
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
                    Image(painter = painterResource(id = R.drawable.ic_forward_filled), contentDescription = null)
                }
            }
        }
        Divider(Modifier.padding(top = 20.dp))
    }
}