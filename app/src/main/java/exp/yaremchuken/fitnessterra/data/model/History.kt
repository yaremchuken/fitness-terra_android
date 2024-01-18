package exp.yaremchuken.fitnessterra.data.model

import java.time.Instant

data class History(
    val id: Long?,
    /**
     * Date and time on which workout is started.
     */
    val startedAt: Instant,

    /**
     * Date and time on which workout is finished.
     */
    val finishedAt: Instant,

    /**
     * Performed workout.
     */
    val workout: Workout
)