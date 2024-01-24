package exp.yaremchuken.fitnessterra.data.datasource.dto.shallow

import exp.yaremchuken.fitnessterra.data.datasource.dto.ExerciseSetupDto
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection

data class WorkoutSectionShallowDto(
    val title: String,
    val setups: List<ExerciseSetupDto>
) {
    companion object {
        fun toSection(dto: WorkoutSectionShallowDto, original: WorkoutSection) =
            WorkoutSection(
                dto.title,
                dto.setups.map {
                    ExerciseSetupDto.fromDto(
                        it,
                        original.setups.find { setup -> setup.exercise.id == it.exerciseId }!!.exercise
                    )
                }
            )
    }
}
