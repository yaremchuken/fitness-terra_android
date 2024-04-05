package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.Equipment
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.data.model.ExerciseSwitchType
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.element.GifImage
import exp.yaremchuken.fitnessterra.ui.theme.AppColor
import exp.yaremchuken.fitnessterra.ui.theme.AppType
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.util.Utils
import kotlin.text.StringBuilder

data class NextExerciseDto (
    val workout: Workout,
    val sectionIdx: Int,
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
    var showWorkoutPlanDialog by remember { mutableStateOf(false) }

    val section = dto.workout.sections[dto.sectionIdx]
    val setup = section.setups[dto.setupIdx]
    val exercise = setup.exercise

    if (!spoken) {
        val speech: StringBuilder = java.lang.StringBuilder()

        if (dto.isSideSwitch) {
            speech.append(stringResource(id = R.string.speak_side_switch)).append(" ")
        }

        if (dto.isNewSection) {
            speech.append(
                stringResource(id = R.string.speak_next_section_block)
                    .replace(":section", section.title)
            ).append(" ")
        }

        if (dto.isNewExercise) {
            speech.append(
                stringResource(id = R.string.speak_next_exercise_block)
                    .replace(":exercise", exercise.title)
                    .replace(":weight",
                        getWeightSpeak(setup.equipment, stringResource(R.string.grams_divider))
                    )
                    .replace(":sets", "${setup.sets.size}")
                    .replace(":set", "${setup.sets[dto.setIdx]}")
                    .replace(":side",
                        if (setup.exercise.sideSwitchType != ExerciseSwitchType.SIDE_SWITCH_ON_SET) ""
                        else stringResource(id = R.string.speak_each_side)
                    )
            ).append(" ")
        } else if (!dto.isSideSwitch){
            speech.append(
                stringResource(id = R.string.speak_next_set_block)
                    .replace(":setnum", "${dto.setIdx + 1}")
                    .replace(":settotal", "${setup.sets.size}")
                    .replace(":set", "${setup.sets[dto.setIdx]}")
            ).append(" ")

            if (setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_REPEAT) {
                speech.append(stringResource(id = R.string.speak_each_side)).append(" ")
            }
        }

        speakOut(speech.toString())
        spoken = true
    }

    Box {
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
                GifImage(Utils.exerciseGifPath(exercise))
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(all = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_workout_plan_btn),
                contentDescription = null,
                Modifier
                    .height(48.dp)
                    .clickable {
                        showWorkoutPlanDialog = !showWorkoutPlanDialog
                    }
            )
        }
        if (showWorkoutPlanDialog) {
            Dialog(
                onDismissRequest = { showWorkoutPlanDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                WorkoutPlanDialog(workout = dto.workout, sectionIdx = dto.sectionIdx, setupIdx = dto.setupIdx)
            }
        }
    }
}

@Composable
fun WorkoutPlanDialog(
    workout: Workout,
    sectionIdx: Int,
    setupIdx: Int
) {
    val scrollState = rememberScrollState()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        Modifier
            .padding(all = 12.dp)
            .background(
                color = AppColor.LightestGray,
                shape = UIConstants.ROUNDED_CORNER
            )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                )
        ) {
            IconButton(
                onClick = { onBackPressedDispatcher?.onBackPressed() },
                Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 6.dp, bottom = 6.dp, start = 6.dp)
                    .width(30.dp)
                    .height(30.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_back_filled), contentDescription = null)
            }
            Text(
                text = stringResource(R.string.workout_plan_title),
                Modifier.align(Alignment.Center),
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .background(
                    color = Color.White,
                    shape = UIConstants.ROUNDED_CORNER
                )
        ) {
            workout.sections.forEachIndexed { secIdx, section ->
                Column {
                    Text(
                        text = section.title,
                        Modifier.padding(all = 12.dp),
                        style = Typography.titleLarge
                    )
                    section.setups.forEachIndexed { setIdx, setup ->
                        ExerciseBlock(
                            setup = setup,
                            isCompleted = secIdx < sectionIdx || (secIdx == sectionIdx && setIdx < setupIdx),
                            isCurrent =  secIdx == sectionIdx && setIdx == setupIdx,
                            isLast = secIdx == workout.sections.size-1 && setIdx == section.setups.size-1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseBlock(
    setup: ExerciseSetup,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
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
        Modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = if (isCurrent) R.drawable.ic_exercise_current else R.drawable.ic_exercise_completed),
                contentDescription = null,
                Modifier
                    .width(32.dp)
                    .padding(horizontal = 6.dp),
                alpha = if (isCurrent || isCompleted) 1F else 0F
            )
            GifImage(
                Utils.exerciseGifPath(setup.exercise),
                Modifier
                    .height(64.dp)
                    .clip(UIConstants.ROUNDED_CORNER)
                    .alpha(alpha = if (isCompleted) .7F else 1F)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .alpha(alpha = if (isCompleted) .7F else 1F),
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
                        if (setup.equipment.sumOf { it.weight } > 0) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_weight),
                                contentDescription = null,
                                Modifier
                                    .height(14.dp)
                                    .padding(start = 12.dp, end = 4.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            setup.equipment.filter { it.weight > 0 }.forEach {
                                Text(
                                    text = "${(if (it.quantity > 1) "${it.quantity}X" else "")}${it.weight * .001}",
                                    style = Typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
        if (!isLast) {
            Divider(Modifier.padding(vertical = 6.dp))
        } else {
            Spacer(Modifier.padding(vertical = 6.dp))
        }
    }
}

private fun allRepeatsAreSame(repeats: List<Long>) = repeats.find { it != repeats[0] } == null

private fun getWeightSpeak(equipment: List<Equipment>, gramsDivider: String): String {
    val kg = equipment.sumOf { it.weight } / 1000
    val gr = equipment.sumOf { it.weight } - kg * 1000
    return "$kg${if (gr > 0) " $gramsDivider ${gr/100}" else ""}"
}