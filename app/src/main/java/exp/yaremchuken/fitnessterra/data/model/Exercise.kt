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
    val description: String,

    /**
     * Muscle group affected by exercise.
     */
    val muscleGroup: MuscleGroupType?,

    /**
     * Additional muscles affected by exercise.
     */
    val muscles: List<MuscleGroupType> = listOf(),

    /**
     * Equipment used during the exercise.
     */
    val equipment: List<EquipmentBase> = listOf(),

    /**
     * Does the trainee have to switch sides, while performing exercises.
     */
    val sideSwitchType: ExerciseSwitchType = ExerciseSwitchType.NO_SIDE_SWITCH,

    /**
     * Steps of exercise performing.
     */
    val steps: List<String> = listOf(),

    /**
     * Tips and advises about exercise perform.
     */
    val advises: List<String> = listOf(),

    /**
     * Some warnings about incorrect performing of exercise and danger it can present.
     */
    val warnings: List<String> = listOf(),

    /**
     * Time, in which one repeat of exercise have to be performed.
     */
    val performTime: Duration = 0.seconds
)
