package exp.yaremchuken.fitnessterra.model

import java.time.LocalDateTime

/**
 * Schedule for workout to perform.
 * By default workout takes 30-minute slot at calendar.
 * In current state only on workout can be set to specified time period.
 */
data class Schedule(
    /**
     * Date and time on which workout is scheduled.
     */
    val scheduledAt: LocalDateTime,

    /**
     * Scheduled workout.
     */
    val workout: Workout
)
