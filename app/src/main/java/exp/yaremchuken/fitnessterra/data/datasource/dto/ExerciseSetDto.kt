package exp.yaremchuken.fitnessterra.data.datasource.dto

import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import kotlin.time.Duration.Companion.milliseconds

class ExerciseSetDto {
    var exerciseId: Long = -1
    var weight: Long = 0
    var repeats: List<Long> = listOf()
    var duration: Long = 0
    var recovery: Long = 0

    companion object {
        fun fromDto(dto: ExerciseSetDto, exercise: Exercise) =
            ExerciseSet(
                exercise,
                dto.weight,
                dto.repeats,
                dto.duration.milliseconds,
                dto.recovery.milliseconds
            )

        fun toDto(set: ExerciseSet): ExerciseSetDto {
            val dto = ExerciseSetDto()
            dto.exerciseId = set.exercise.id
            dto.weight = set.weight
            dto.repeats = set.repeats
            dto.duration = set.duration.inWholeMilliseconds
            dto.recovery = set.recovery.inWholeMilliseconds
            return dto
        }
    }
}