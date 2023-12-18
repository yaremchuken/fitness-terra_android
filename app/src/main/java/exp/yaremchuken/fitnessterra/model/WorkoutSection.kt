package exp.yaremchuken.fitnessterra.model

import kotlin.time.Duration

/**
 * Workout section it's logical part of workout, containing similar exercise sets,
 * like workout warmup section, or section contains exercises for specific muscle group or body part.
 */
data class WorkoutSection(

    /**
     * Name of this section, like 'warmup', 'arms' etc.
     */
    val title: String,

    /**
     * Workout unit consist of one or multiply exercise sets.
     */
    val sets: List<ExerciseSet>,

    /**
     * Amount ot time to rest between exercise sets.
     * List must be empty, or contain single or exerciseSets.size-1 values,
     * single value means that all rests have same duration.
     */
    val recovery: List<Duration> = listOf()
) {
    companion object {
        fun totalDuration(section: WorkoutSection): Duration {
            var total = Duration.ZERO
            if (section.sets.isNotEmpty()) {
                total = total.plus(
                    section.sets.map { ExerciseSet.totalDuration(it) }.reduce { acc, duration -> acc.plus(duration) }
                )
            }

            if (section.recovery.size == 1) {
                total = total.plus(section.recovery[0].times(section.sets.size - 1))
            } else if (section.recovery.isNotEmpty()) {
                total = total.plus(section.recovery.reduce { acc, duration -> acc.plus(duration) })
            }

            return total
        }
    }
}
