package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.element.GifImage
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils

data class NextExerciseDto (
    val section: WorkoutSection,
    val exercise: Exercise,
    val setupIdx: Int,
    val setIdx: Int,
    val isNewSection: Boolean,
    val isNewExercise: Boolean
)

@Composable
fun NextExerciseBlock(
    speakOut: (text: String) -> Unit,
    dto: NextExerciseDto
) {
    var spoken by remember { mutableStateOf(false) }

    val setup = dto.section.setups[dto.setupIdx]

    if (!spoken) {
        if (dto.isNewSection) {
            speakOut(
                stringResource(id = R.string.speak_next_section_block)
                    .replace(":section", dto.section.title)
            )
        }
        if (dto.isNewExercise) {
            speakOut(
                stringResource(id = R.string.speak_next_exercise_block)
                    .replace(":exercise", dto.exercise.title)
                    .replace(":weight", "${setup.weight/1000}")
                    .replace(":sets", "${setup.sets.size}")
                    .replace(":set", "${setup.sets[dto.setIdx]}")
            )
        } else {
            speakOut(
                stringResource(id = R.string.speak_next_set_block)
                    .replace(":set", "${setup.sets[dto.setIdx]}")
            )
        }
        spoken = true
    }

    val setInfo =
        """
            ${stringResource(R.string.set_title)} ${dto.setIdx + 1} ${stringResource(id = R.string.from)} ${setup.sets.size}
             - x${setup.sets[dto.setIdx]} ${stringResource(R.string.repeats_title)}
        """.trimIndent().replace("\n", "")

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
            GifImage(Utils.exerciseGifPath(dto.exercise))
        }
        Column (
            Modifier
                .padding(horizontal = 12.dp)
        ) {
            Column(
                Modifier
                    .background(
                        color = AppColor.GrayTransparent,
                        shape = UIConstants.ROUNDED_CORNER
                    )
                    .alpha(.7F)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = UIConstants.ROUNDED_CORNER
                    )
                    .padding(all = 6.dp)
            ) {
                Text(
                    text = setup.exercise.title,
                    Modifier.padding(start = 8.dp),
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (setup.sets.isNotEmpty()) {
                    Row {
                        Text(
                            text = setInfo,
                            Modifier.padding(start = 8.dp),
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}