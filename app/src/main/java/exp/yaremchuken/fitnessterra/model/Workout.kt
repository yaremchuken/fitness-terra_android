package exp.yaremchuken.fitnessterra.model

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
     * Exercises included in this workout.
     */
    val sets: List<ExerciseSet>,

    /**
     * Pauses between exercise sets in seconds.
     */
    val rests: List<Int>
)