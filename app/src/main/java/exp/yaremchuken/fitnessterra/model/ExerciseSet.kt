package exp.yaremchuken.fitnessterra.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Repetitions of a specific Exercise as a part of Workout.
 */
data class ExerciseSet(
    /**
     * Type of exercise in set.
     */
    val exercise: Exercise,

    /**
     * Amount of weight used in sets in grams.
     */
    val weight: Long = 0,

    /**
     * Amount of repeats for exercise in this Set.
     * Not used if the durations is specified.
     */
    val repeats: List<Long> = listOf(),

    /**
     * Durations of exercise performing in seconds.
     * Not used if the repeats is specified.
     */
    val durations: List<Duration> = listOf(),

    /**
     * Amount ot time to rest between exercises.
     * List must be empty, or contain single or (repeats/duration).size-1 values,
     * single value means that all rests have same duration.
     */
    val recovery: List<Duration> = listOf()
) {
    companion object {
        /**
         * Get total duration of this set, including recovery time
         */
        fun totalDuration(set: ExerciseSet): Duration {
            var total = Duration.ZERO
            // Lets make assumption that it takes two seconds for one repetition
            total = total.plus((set.repeats.sum() * 2).seconds)
            if (set.durations.isNotEmpty()) {
                total = total.plus(set.durations.reduce { acc, duration -> acc.plus(duration) })
            }

            if (set.recovery.size == 1) {
                total = total.plus(set.recovery[0].times(set.repeats.size + set.durations.size - 1))
            } else if (set.recovery.isNotEmpty()) {
                total = total.plus(set.recovery.reduce { acc, duration -> acc.plus(duration) })
            }

            return total
        }
    }
}
