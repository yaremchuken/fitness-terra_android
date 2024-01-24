package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.data.model.WorkoutType

class WorkoutDto {
    var id: Long = -1
    lateinit var title: Map<String, String>
    lateinit var type: WorkoutType
    lateinit var sections: List<WorkoutSectionDto>

    companion object {
        fun fromDto(dto: WorkoutDto, exercises: List<Exercise>) =
            Workout(
                dto.id,
                dto.title[AppSettings.locale().language]!!,
                dto.type,
                dto.sections.map { sec ->
                    WorkoutSection(
                        sec.title[AppSettings.locale().language]!!,
                        sec.setups.map { setup ->
                            ExerciseSetupDto.fromDto(
                                setup,
                                exercises.find { ex -> ex.id == setup.exerciseId }!!
                            )
                        }
                    )
                }
            )
    }
}