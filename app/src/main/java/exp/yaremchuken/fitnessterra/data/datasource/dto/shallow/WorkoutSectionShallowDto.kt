package exp.yaremchuken.fitnessterra.data.datasource.dto.shallow

import exp.yaremchuken.fitnessterra.data.datasource.dto.ExerciseSetDto
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection

data class WorkoutSectionShallowDto(
    val title: String,
    val sets: List<ExerciseSetDto>
) {
    companion object {
        fun toSection(dto: WorkoutSectionShallowDto, original: WorkoutSection) =
            WorkoutSection(
                dto.title,
                dto.sets.map {
                    ExerciseSetDto.fromDto(
                        it,
                        original.sets.find { set -> set.exercise.id == it.exerciseId }!!.exercise
                    )
                }
            )
    }
}
