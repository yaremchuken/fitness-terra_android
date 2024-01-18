package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.data.model.WorkoutType
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class WorkoutDto {
    var id: Long = -1
    lateinit var title: Map<Locale, String>
    lateinit var type: WorkoutType
    lateinit var sections: List<WorkoutSectionDto>

    companion object {
        fun fromDto(dto: WorkoutDto, exercises: List<Exercise>) =
            Workout(
                dto.id,
                dto.title[AppSettings.locale()]!!,
                dto.type,
                dto.sections.map { sec ->
                    WorkoutSection(
                        sec.title[AppSettings.locale()]!!,
                        sec.sets.map { set ->
                            ExerciseSetDto.fromDto(
                                set,
                                exercises.find { ex -> ex.id == set.exerciseId }!!
                            )
                        }
                    )
                }
            )
    }
}