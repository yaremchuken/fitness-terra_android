package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.data.model.EquipmentBase
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSwitchType
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import kotlin.time.Duration.Companion.milliseconds

class ExerciseDto {
    var id: Long = -1
    lateinit var title: Map<String, String>
    lateinit var description: Map<String, String>
    var muscleGroup: MuscleGroupType? = null
    var muscles: List<MuscleGroupType> = listOf()
    var equipment: List<EquipmentBase> = listOf()
    var sideSwitchType: ExerciseSwitchType? = null
    var steps: Map<String, List<String>> = mapOf()
    var advises: Map<String, List<String>> = mapOf()
    var warnings: Map<String, List<String>> = mapOf()
    var performTime: Long = 0

    companion object {
        fun fromDto(dto: ExerciseDto) =
            Exercise(
                dto.id,
                dto.title[AppSettings.locale().language]!!,
                dto.description[AppSettings.locale().language]!!,
                dto.muscleGroup,
                dto.muscles,
                dto.equipment,
                dto.sideSwitchType ?: ExerciseSwitchType.NO_SIDE_SWITCH,
                dto.steps[AppSettings.locale().language]!!,
                dto.advises[AppSettings.locale().language] ?: listOf(),
                dto.warnings[AppSettings.locale().language] ?: listOf(),
                dto.performTime.milliseconds
            )
    }
}