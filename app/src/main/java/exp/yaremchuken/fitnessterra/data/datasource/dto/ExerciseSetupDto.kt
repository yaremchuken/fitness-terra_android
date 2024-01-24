package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import kotlin.time.Duration.Companion.milliseconds

class ExerciseSetupDto {
    var exerciseId: Long = -1
    var weight: Long = 0
    var sets: List<Long> = listOf()
    var duration: Long = 0
    var recovery: Long = 0

    companion object {
        fun fromDto(dto: ExerciseSetupDto, exercise: Exercise) =
            ExerciseSetup(
                exercise,
                dto.weight,
                dto.sets,
                dto.duration.milliseconds,
                dto.recovery.milliseconds
            )

        fun toDto(setup: ExerciseSetup): ExerciseSetupDto {
            val dto = ExerciseSetupDto()
            dto.exerciseId = setup.exercise.id
            dto.weight = setup.weight
            dto.sets = setup.sets
            dto.duration = setup.duration.inWholeMilliseconds
            dto.recovery = setup.recovery.inWholeMilliseconds
            return dto
        }
    }
}