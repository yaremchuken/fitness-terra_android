package exp.yaremchuken.fitnessterra.view.perform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exp.yaremchuken.fitnessterra.model.ExerciseSet
import exp.yaremchuken.fitnessterra.model.Workout
import exp.yaremchuken.fitnessterra.view.animation.ExerciseAnimation
import exp.yaremchuken.fitnessterra.view.perform.WorkoutPerformState.COMPLETED
import exp.yaremchuken.fitnessterra.view.perform.WorkoutPerformState.GET_READY
import exp.yaremchuken.fitnessterra.view.perform.WorkoutPerformState.PERFORM
import exp.yaremchuken.fitnessterra.view.perform.WorkoutPerformState.RECOVERY
import exp.yaremchuken.fitnessterra.view.workout.workoutStub
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun WorkoutPerformView(
    workout: Workout = workoutStub
) {
    var state by remember { mutableStateOf(PERFORM) }

    var sectionIdx by remember { mutableIntStateOf(0) }
    var setIdx by remember { mutableIntStateOf(0) }
    var repeatIdx by remember { mutableIntStateOf(0) }

    var section = workout.sections[sectionIdx]
    var exerciseSet = section.sets[setIdx]

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
                RECOVERY -> WorkoutPerformRecoveryBlock(
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
                    duration = getRecoveryAfterCompleteExercise(exerciseSet, repeatIdx)
                )
                else -> Box { }
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
                    exercise = exerciseSet.exercise
                )
                PERFORM -> PerformBlock(
                    onFinish = {
                        if (sectionIdx == workout.sections.size-1 &&
                            setIdx == section.sets.size-1 &&
                            repeatIdx >= exerciseSet.repeats.size-1
                        ) {
                            state = COMPLETED
                        } else {
                            state = RECOVERY
                        }
                    },
                    set = exerciseSet,
                    repeatIdx = if (exerciseSet.repeats.isNotEmpty()) repeatIdx else -1,
                    totalRepeats = exerciseSet.repeats.size
                )
                RECOVERY -> NextExerciseBlock(
                    section = section,
                    exercise = exerciseSet.exercise,
                    currIdx = setIdx + 1,
                    total = exerciseSet.repeats.size + (if (exerciseSet.duration > 0.seconds) 1 else 0)
                )
                else -> Box { }
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