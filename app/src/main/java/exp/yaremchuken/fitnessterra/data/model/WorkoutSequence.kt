package exp.yaremchuken.fitnessterra.data.model

import java.time.DayOfWeek
import java.time.Instant

data class WorkoutSequence(
    /**
     * Time on which this sequence is scheduled.
     */
    val scheduledAt: Instant,

    /**
     * Workouts in sequence.
     */
    val workouts: List<Workout>,

    /**
     * Days of week on which this sequence workouts supposed be performed.
     */
    val weekdays: List<DayOfWeek>,

    /**
     * Minimum amount of days6 that should pass between scheduled workouts.
     */
    val daysSpan: Int = 0
)