package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.animation.ExerciseAnimation
import exp.yaremchuken.fitnessterra.util.Utils

data class NextExerciseDto (
    val section: WorkoutSection,
    val exercise: Exercise,
    val setIdx: Int,
    val repeatIdx: Int
)

@Composable
fun NextExerciseBlock(
    speakOut: (text: String) -> Unit,
    dto: NextExerciseDto
) {
    var spoken by remember { mutableStateOf(false) }

    val set = dto.section.sets[dto.setIdx]

    if (!spoken) {
        speakOut(
            stringResource(id = R.string.speak_next_exercise_block)
                .replace("%s", dto.section.title)
                .replace("%e", dto.exercise.title)
                .replace("%r", "${set.repeats[dto.repeatIdx]}")
        )
        spoken = true
    }

    Box(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExerciseAnimation(dto.exercise, Modifier)
        }
        Column {
            Row(
                Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.workout_perform_next_is),
                    Modifier.padding(end = 8.dp),
                    style = Typography.titleLarge
                )
                Text(
                    text = dto.section.title,
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            if (set.repeats.isNotEmpty()) {
                Row(
                    Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = "${stringResource(R.string.repeat_title)} ${dto.repeatIdx + 1} ${stringResource(id = R.string.from)} ${set.repeats.size}",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = exerciseLine(set, dto.repeatIdx),
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun exerciseLine(set: ExerciseSet, repeatIdx: Int): String {
    val title = set.exercise.title
    val block =
        if (set.repeats.isNotEmpty()) "x${set.repeats[repeatIdx]}"
        else Utils.formatToTime(set.duration)
    return "$title $block"
}