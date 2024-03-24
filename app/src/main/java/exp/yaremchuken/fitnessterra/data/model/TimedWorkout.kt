package exp.yaremchuken.fitnessterra.data.model

import java.time.LocalTime

/**
 * Workout with Time its supposed to performed.
 */
data class TimedWorkout(
    /**
     * Time of day this workout should be performed.
     */
    val scheduledAt: LocalTime,

    /**
     * Workout that should be performed.
     */
    val workout: Workout
)
