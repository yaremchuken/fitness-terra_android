package exp.yaremchuken.fitnessterra.data.model

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
     * Not needed if the duration is specified.
     */
    val repeats: List<Long> = listOf(),

    /**
     * Duration of exercise performing.
     * Not needed if the repeats is specified.
     */
    val duration: Duration = 0.seconds,

    /**
     * Amount ot time to rest after performing this set.
     */
    val recovery: Duration = 0.seconds
) {
    /**
     * Get total duration of this set, including recovery time.
     */
    fun totalDuration(): Duration =
        exercise.repeatTime.times(repeats.sum().toInt())
            .plus(duration)
            .plus(exercise.recovery.times((repeats.size-1).coerceAtLeast(0)))
}
