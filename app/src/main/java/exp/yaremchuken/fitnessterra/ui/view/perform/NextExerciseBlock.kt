package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Equipment
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSwitchType
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.element.GifImage
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils

data class NextExerciseDto (
    val section: WorkoutSection,
    val exercise: Exercise,
    val setupIdx: Int,
    val setIdx: Int,
    val isSideSwitch: Boolean = false,
    val isNewSection: Boolean = false,
    val isNewExercise: Boolean = false
)

@Composable
fun NextExerciseBlock(
    speakOut: (text: String) -> Unit,
    dto: NextExerciseDto
) {
    var spoken by remember { mutableStateOf(false) }

    val setup = dto.section.setups[dto.setupIdx]

    if (!spoken) {
        if (dto.isSideSwitch) {
            speakOut(
                stringResource(id = R.string.speak_side_switch)
            )
        }
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
                    .replace(":weight", "${setup.equipment.sumOf { it.weight }.toFloat()/1000}")
                    .replace(":sets", "${setup.sets.size}")
                    .replace(":set", "${setup.sets[dto.setIdx]}")
                    .replace(":side",
                        if (setup.exercise.sideSwitchType != ExerciseSwitchType.SIDE_SWITCH_ON_SET) ""
                        else stringResource(id = R.string.speak_each_side)
                    )
            )
        } else if (!dto.isSideSwitch){
            speakOut(
                stringResource(id = R.string.speak_next_set_block)
                    .replace(":setnum", "${dto.setIdx + 1}")
                    .replace(":settotal", "${setup.sets.size}")
                    .replace(":set", "${setup.sets[dto.setIdx]}")
            )
            if (setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_REPEAT) {
                speakOut(stringResource(id = R.string.speak_each_side))
            }
        }
        spoken = true
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column (
            Modifier
                .padding(horizontal = 12.dp)
        ) {
            Column(
                Modifier
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
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (setup.sets.isNotEmpty() || setup.equipment.sumOf { it.weight } > 0) {
                    Row(
                        Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (setup.sets.isNotEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_repeat),
                                contentDescription = null,
                                Modifier.height(24.dp)
                            )
                            setup.sets.forEachIndexed { index, set ->
                                Text(
                                    text = "$set",
                                    Modifier.padding(start = 8.dp),
                                    style = if (index == dto.setIdx) Typography.titleLarge else Typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (index == dto.setIdx) TextDecoration.Underline else TextDecoration.None
                                )}
                        }
                        if (setup.equipment.sumOf { it.weight } > 0) {
                            if (setup.sets.isNotEmpty()) {
                                Text(
                                    text = "|",
                                    Modifier.padding(horizontal = 12.dp),
                                    style = Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_weight),
                                contentDescription = null,
                                Modifier.height(24.dp)
                            )
                            Text(
                                text = "${setup.equipment.sumOf { it.weight }.toFloat()/1000}",
                                Modifier.padding(start = 8.dp),
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GifImage(Utils.exerciseGifPath(dto.exercise))
        }
    }
}

private fun getWeightSpeak(equipment: List<Equipment>, gramsDivider: String): String {
    val kg = equipment.sumOf { it.weight } / 1000
    val gr = equipment.sumOf { it.weight } - kg * 1000
    return "$kg${if (gr > 0) " $gramsDivider ${gr/100}" else ""}"
}