package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class ExerciseDto {
    var id: Long = -1
    lateinit var title: Map<Locale, String>
    lateinit var description: Map<Locale, String>
    var muscleGroup: MuscleGroupType? = null
    var muscles: List<MuscleGroupType> = listOf()
    var equipment: EquipmentType? = null
    var steps: Map<Locale, List<String>> = mapOf()
    var advises: Map<Locale, List<String>> = mapOf()
    var warnings: Map<Locale, List<String>> = mapOf()
    var repeatTime: Long = 0
    var recovery: Long = 0

    companion object {
        fun fromDto(dto: ExerciseDto) =
            Exercise(
                dto.id,
                dto.title[AppSettings.locale()]!!,
                dto.description[AppSettings.locale()]!!,
                dto.muscleGroup,
                dto.muscles,
                dto.equipment,
                dto.steps[AppSettings.locale()]!!,
                dto.advises[AppSettings.locale()] ?: listOf(),
                dto.warnings[AppSettings.locale()] ?: listOf(),
                dto.repeatTime.milliseconds,
                dto.recovery.milliseconds
            )
    }
}