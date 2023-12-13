package exp.yaremchuken.fitnessterra.model

/**
 * Repetitions of a specific Exercise as a part of Workout.
 */
data class ExerciseSet(
    /**
     * Type of exercise in set.
     */
    val exercise: Exercise,

    /**
     * Pauses between exercises in seconds.
     */
    val rests: List<Long>,

    /**
     * Amount of weight used in sets in grams.
     */
    val weight: Long = 0,

    /**
     * Amount of repeats for exercise in this Set.
     * Not used if the durations is specified.
     */
    val repeats: List<Long> = listOf(),

    /**
     * Durations of exercise performing in seconds.
     * Not used if the repeats is specified.
     */
    val durations: List<Long> = listOf()
)
