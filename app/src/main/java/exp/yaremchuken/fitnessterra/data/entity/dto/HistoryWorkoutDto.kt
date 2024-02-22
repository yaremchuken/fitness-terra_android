package exp.yaremchuken.fitnessterra.data.entity.dto

import com.google.gson.Gson
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.data.model.WorkoutType
import kotlin.time.Duration.Companion.seconds

/**
 * Shallow aka minimum info about workout, used for persistence reasons.
 */
data class HistoryWorkoutDto(
    val workoutId: Long,
    val title: String,
    val type: WorkoutType,
    val sections: List<HistoryWorkoutSectionDto>
) {
    companion object {
        fun fromWorkout(workout: Workout) =
            HistoryWorkoutDto(
                workout.id,
                workout.title,
                workout.type,
                workout.sections.map { section ->
                    HistoryWorkoutSectionDto(
                        section.id,
                        section.order,
                        section.title,
                        section.setups.map { setup ->
                            HistoryExerciseSetupDto(
                                section.id,
                                setup.exercise.id,
                                setup.order,
                                setup.equipment,
                                Gson().toJson(setup.sets),
                                setup.duration.inWholeSeconds,
                                setup.recovery.inWholeSeconds
                            )
                        }
                    )
                }
            )
    }

    fun toWorkout(exercises: List<Exercise>) =
        Workout(
            workoutId,
            title,
            type,
            sections.map { section ->
                WorkoutSection(
                    section.id,
                    section.order,
                    section.title,
                    section.setups.map { setup ->
                        ExerciseSetup(
                            section.id,
                            exercises.find { ex -> ex.id == setup.exerciseId }!!,
                            setup.order,
                            setup.equipment,
                            Gson().fromJson(setup.sets, Array<Long>::class.java).toList(),
                            setup.duration.seconds,
                            setup.recovery.seconds
                        )
                    }
                )
            }
        )
}