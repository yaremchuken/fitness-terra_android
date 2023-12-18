package exp.yaremchuken.fitnessterra.model

import kotlin.time.Duration

data class Workout(
    /**
     * Workout unique identification.
     */
    val id: Long,

    /**
     * Workout name.
     */
    val title: String,

    /**
     * Activity type of workout.
     */
    val type: WorkoutType,

    /**
     * Sections included in this workout.
     */
    val sections: List<WorkoutSection> = listOf(),

    /**
     * Amount ot time to rest between workout sections.
     * List must be empty, or contain single or sections.size-1 values,
     * single value means that all rests have same duration.
     */
    val recovery: List<Duration> = listOf()
) {
    companion object {
        fun totalDuration(workout: Workout): Duration {
            var total = Duration.ZERO
            if (workout.sections.isNotEmpty()) {
                total = total.plus(
                    workout.sections
                        .map { WorkoutSection.totalDuration(it) }
                        .reduce { acc, duration -> acc.plus(duration) }
                )
            }
            if (workout.recovery.size == 1) {
                total = total.plus(workout.recovery[0].times(workout.sections.size - 1))
            } else if (workout.recovery.isNotEmpty()) {
                total = total.plus(workout.recovery.reduce { acc, duration -> acc.plus(duration) })
            }

            return total
        }
    }
}