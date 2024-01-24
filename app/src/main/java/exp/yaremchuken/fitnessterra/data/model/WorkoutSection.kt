package exp.yaremchuken.fitnessterra.data.model

import kotlin.time.Duration

/**
 * Workout section it's logical part of workout, containing similar exercises,
 * like workout warmup section, or section contains exercises for specific muscle group or body part.
 */
data class WorkoutSection(

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
        setups.forEachIndexed { index, set ->
            total = total.plus(set.totalDuration())
            if (index != setups.size-1) {
                total = total.plus(set.recovery)
            }
        }
        return total
    }
}
