package exp.yaremchuken.fitnessterra.data.datasource.dto.shallow

import exp.yaremchuken.fitnessterra.data.datasource.dto.ExerciseSetDto
import exp.yaremchuken.fitnessterra.data.model.Workout

/**
 * Shallow aka minimum info about workout, used for persistence reasons.
 */
data class WorkoutShallowDto(
    val sections: List<WorkoutSectionShallowDto>
) {
    companion object {
        fun fromWorkout(workout: Workout) =
            WorkoutShallowDto(
                workout.sections.map { section ->
                    WorkoutSectionShallowDto(
                        section.title,
                        section.sets.map { set ->
                            ExerciseSetDto.toDto(set)
                        }
                    )
                }
            )

        fun toWorkout(dto: WorkoutShallowDto, original: Workout) =
            Workout(
                original.id,
                original.title,
                original.type,
                dto.sections.map { section ->
                    WorkoutSectionShallowDto.toSection(
                        section,
                        original.sections.find { sc -> sc.title == section.title }!!
                    )
                }
            )
    }
}