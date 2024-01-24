package exp.yaremchuken.fitnessterra.data.model

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
    val sections: List<WorkoutSection> = listOf()
) {
    fun totalDuration(): Duration {
        var total = Duration.ZERO
        sections.forEachIndexed { index, section ->
            total = total.plus(section.totalDuration())
            if (index != sections.size-1) {
                total = total.plus(section.setups[section.setups.size-1].recovery)
            }
        }
        return total
    }
}