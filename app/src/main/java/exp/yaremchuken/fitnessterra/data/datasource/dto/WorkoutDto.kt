package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.data.model.WorkoutType
import java.util.Locale

class WorkoutDto {
    var id: Long = -1
    lateinit var title: Map<Locale, String>
    lateinit var type: WorkoutType
    lateinit var sections: List<WorkoutSectionDto>
}