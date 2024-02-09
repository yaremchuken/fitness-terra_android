package exp.yaremchuken.fitnessterra.data.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Exercise with modifiable setup as a part of Workout.
 */
data class ExerciseSetup(
    /**
     * Section of workout.
     */
    val sectionId: Long,

    /**
     * Type of exercise.
     */
    val exercise: Exercise,

    /**
     * Position of this exercise in section.
     */
    val order: Long,

    /**
     * Amount of weight used in sets in grams.
     */
    val weight: Long = 0,

    /**
     * Amount of repeats for exercise.
     * Not needed if the duration is specified.
     */
    val sets: List<Long> = listOf(),

    /**
     * Duration of exercise performing.
     * Not needed if the repeats is specified.
     */
    val duration: Duration = 0.seconds,

    /**
     * Amount ot time to rest after performing this setup.
     */
    val recovery: Duration = 0.seconds
) {
    /**
     * Get total duration of this set, including recovery time.
     */
    fun totalDuration(): Duration =
        exercise.performTime.times(sets.sum().toInt())
            .plus(duration)
            .plus(recovery.times((sets.size-1).coerceAtLeast(0)))
}
