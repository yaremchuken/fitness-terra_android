package exp.yaremchuken.fitnessterra.ui.view.perform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exp.yaremchuken.fitnessterra.R
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import exp.yaremchuken.fitnessterra.ui.UIConstants
import exp.yaremchuken.fitnessterra.ui.theme.Typography
import exp.yaremchuken.fitnessterra.ui.view.animation.ExerciseAnimation
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformState.COMPLETED
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformState.GET_READY
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformState.PERFORM
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformState.RECOVERY
import exp.yaremchuken.fitnessterra.viewmodel.WorkoutPerformViewModel
import kotlin.time.Duration

@Composable
fun WorkoutPerformScreen(
    goHome: () -> Unit,
    workoutId: Long,
    viewModel: WorkoutPerformViewModel = hiltViewModel()
) {
    var state by remember { mutableStateOf(GET_READY) }

    var sectionIdx by remember { mutableIntStateOf(0) }
    var setIdx by remember { mutableIntStateOf(0) }
    var repeatIdx by remember { mutableIntStateOf(0) }

    val workout = viewModel.getWorkout(workoutId)!!
    var section = workout.sections[sectionIdx]
    var exerciseSet = section.sets[setIdx]

    viewModel.markStart()

    val workoutCompletedText = stringResource(id = R.string.speak_workout_completed)

    if (state == GET_READY) {
        viewModel.speakWorkoutBegin(stringResource(id = R.string.speak_workout_begin_template), workout)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalArrangement = Arrangement.Center
        ) {
            when(state) {
                GET_READY, PERFORM -> ExerciseAnimation(
                    exercise = exerciseSet.exercise,
                    modifier = Modifier.fillMaxWidth()
                )
                RECOVERY -> WorkoutRecoveryBlock(
                    onFinish = {
                        repeatIdx++
                        if (repeatIdx >= exerciseSet.repeats.size) {
                            repeatIdx = 0
                            setIdx++
                            if (setIdx >= section.sets.size) {
                                setIdx = 0;
                                sectionIdx++
                            }
                        }
                        section = workout.sections[sectionIdx]
                        exerciseSet = section.sets[setIdx]
                        state = PERFORM
                    },
                    speakOut = { viewModel.speakOut(it) },
                    duration = getRecoveryAfterCompleteExercise(exerciseSet, repeatIdx)
                )
                COMPLETED -> Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "FINISHED")
                }
            }
        }
        Divider(Modifier.padding(all = 12.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            when(state) {
                GET_READY -> GetReadyBlock(
                    onFinish = { state = PERFORM },
                    speakOut = { viewModel.speakOut(it) },
                    exercise = exerciseSet.exercise
                )
                PERFORM -> PerformBlock(
                    onFinish = {
                        state =
                            if (sectionIdx == workout.sections.size-1 &&
                                setIdx == section.sets.size-1 &&
                                repeatIdx >= exerciseSet.repeats.size-1
                            ) {
                                viewModel.persistHistory(workout)
                                viewModel.speakOut(workoutCompletedText)
                                COMPLETED
                            } else {
                                RECOVERY
                            }
                    },
                    speakOut = { viewModel.speakOut(it) },
                    set = exerciseSet,
                    repeatIdx = if (exerciseSet.repeats.isNotEmpty()) repeatIdx else -1,
                    totalRepeats = exerciseSet.repeats.size
                )
                RECOVERY -> NextExerciseBlock(
                    speakOut = { viewModel.speakOut(it) },
                    viewModel.getNextExerciseDto(workout, sectionIdx, setIdx, repeatIdx)
                )
                COMPLETED -> Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { goHome() },
                        shape = UIConstants.ROUNDED_CORNER
                    ) {
                        Text(
                            text = stringResource(R.string.finish_title),
                            Modifier.padding(vertical = 4.dp),
                            style = Typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * Recovery time between exercises.
 * For last exercise in set we take recovery time for set,
 * and for the last set in section we will take recovery for set too.
 * The idea is than we don't need special recovery time for between sections,
 * and it's ok to use set recovery time between sections.
 */
fun getRecoveryAfterCompleteExercise(set: ExerciseSet, repeatIdx: Int): Duration {
    if (repeatIdx == set.repeats.size-1) {
        return set.recovery
    }
    return set.exercise.recovery
}