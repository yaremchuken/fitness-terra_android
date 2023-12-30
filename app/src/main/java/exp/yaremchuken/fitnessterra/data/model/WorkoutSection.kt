package exp.yaremchuken.fitnessterra.data.model

import kotlin.time.Duration

/**
 * Workout section it's logical part of workout, containing similar exercise sets,
 * like workout warmup section, or section contains exercises for specific muscle group or body part.
 */
data class WorkoutSection(

    /**
     * Name of this section, like 'warmup', 'arms' etc.
     */
    val title: String,

    /**
     * Workout unit consist of one or multiply exercise sets.
     */
    val sets: List<ExerciseSet>
) {
    /**
     * Get total duration of this section, including recovery time.
     */
    fun totalDuration(): Duration {
        var total = Duration.ZERO
        sets.forEachIndexed { index, set ->
            total = total.plus(set.totalDuration())
            if (index != sets.size-1) {
                total = total.plus(set.recovery)
            }
        }
        return total
    }
}
