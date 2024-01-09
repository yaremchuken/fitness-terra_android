package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings
import java.util.Locale
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
    val title: Map<Locale, String>,

    /**
     * A couple of words about this exercise.
     */
    val description: Map<Locale, String> = mapOf(),

    /**
     * Muscle group affected by exercise.
     */
    val muscleGroup: MuscleGroupType?,

    /**
     * Additional muscles affected by exercise.
     */
    val muscles: List<MuscleGroupType> = listOf(),

    /**
     * Type of equipment using during the exercise.
     */
    val equipment: EquipmentType?,

    /**
     * Steps of exercise performing.
     */
    val steps: Map<Locale, List<String>> = mapOf(),

    /**
     * Tips and advises about exercise perform.
     */
    val advises: Map<Locale, List<String>> = mapOf(),

    /**
     * Some warnings about incorrect performing of exercise and danger it can present.
     */
    val warnings: Map<Locale, List<String>> = mapOf(),

    /**
     * Time, in which one repeat of exercise have to be performed.
     */
    val repeatTime: Duration = 2.seconds,

    /**
     * Amount ot time person needs to rest after performing this exercise.
     */
    val recovery: Duration = 20.seconds
) {
    fun titleLocalized() = title[AppSettings.locale()]!!
    fun descriptionLocalized() = description[AppSettings.locale()]!!
    fun stepsLocalized() = steps[AppSettings.locale()]!!
    fun advisesLocalized() = advises[AppSettings.locale()]!!
    fun warningsLocalized() = warnings[AppSettings.locale()]!!
}
