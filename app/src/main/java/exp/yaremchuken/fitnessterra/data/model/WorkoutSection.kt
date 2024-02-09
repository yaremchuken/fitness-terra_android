package exp.yaremchuken.fitnessterra.data.model

import kotlin.time.Duration

/**
 * Workout section it's logical part of workout, containing similar exercises,
 * like workout warmup section, or section contains exercises for specific muscle group or body part.
 */
data class WorkoutSection(
    /**
     * Workout section unique identification.
     */
    val id: Long,

    /**
     * Position of this section in workout.
     */
    val order: Long,

    /**
     * Name of this section, like 'Warmup', 'Biceps' etc.
     */
    val title: String,

    /**
     * Workout unit consist of one or multiply exercise setups.
     */
    val setups: List<ExerciseSetup>
) {
    /**
     * Get total duration of this section, including recovery time.
     */
    fun totalDuration(): Duration {
        var total = Duration.ZERO
        setups.forEachIndexed { index, setup ->
            total = total.plus(setup.totalDuration())
            if (index != setups.size-1) {
                total = total.plus(setup.recovery)
            }

            if (setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_SET) {
                total = total.plus(setup.totalDuration())
                total = total.plus(setup.recovery)
            }
        }
        return total
    }
}
