package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.MuscleGroupType
import java.util.Locale

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
}