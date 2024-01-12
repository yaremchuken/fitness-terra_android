package exp.yaremchuken.fitnessterra.data.model

import java.time.DayOfWeek
import java.time.Instant

/**
 * Schedule for workout to perform.
 * By default workout takes 30-minutes slot at calendar.
 * In current state only one workout can be set to specified time period.
 */
data class Schedule(
    val id: Long?,
    /**
     * Date and time on which workout is scheduled.
     */
    val scheduledAt: Instant,

    /**
     * Scheduled workout.
     */
    val workout: Workout,

    /**
     * Days of week on which this workout must be performed repeatably.
     */
    val weekdays: List<DayOfWeek>
)
