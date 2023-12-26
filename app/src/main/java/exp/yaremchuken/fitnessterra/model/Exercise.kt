package exp.yaremchuken.fitnessterra.model

data class Exercise(
    /**
     * Exercise unique identification.
     */
    val id: Long,

    /**
     * Exercise name.
     */
    val title: String,

    /**
     * A couple of words about this exercise.
     */
    val description: String?,

    /**
     * Main muscle group affected by exercise.
     */
    val muscleGroup: MuscleGroup?,

    /**
     * Type of equipment using during the exercise.
     */
    val equipment: EquipmentType?,

    /**
     * Steps of exercise performing.
     */
    val steps: List<String> = listOf(),

    /**
     * Time exercise needs to be performed, milliseconds.
     */
    val performingTime: Long,

    /**
     * Tips and advises about exercise perform.
     */
    val advise: String?,

    /**
     * Some warnings about incorrect performing of exercise and danger it can present.
     */
    val warnings: List<String> = listOf()
)
