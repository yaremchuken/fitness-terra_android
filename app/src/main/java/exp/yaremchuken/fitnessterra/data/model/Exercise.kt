package exp.yaremchuken.fitnessterra.data.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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
     * Time, in which one repeat of exercise have to be performed.
     */
    val repeatTime: Duration = 0.seconds,

    /**
     * Amount ot time person needs to rest after performing this exercise.
     */
    val recovery: Duration = 0.seconds,

    /**
     * Tips and advises about exercise perform.
     */
    val advise: String?,

    /**
     * Some warnings about incorrect performing of exercise and danger it can present.
     */
    val warnings: List<String> = listOf()
)
