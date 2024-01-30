package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.WorkoutDao
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import exp.yaremchuken.fitnessterra.util.Utils

class WorkoutRepository(
    private val dao: WorkoutDao
) {
    suspend fun insert(entity: WorkoutEntity) = dao.insert(entity)

    fun getById(id: Long) = dao.getById(id)

    fun getAll() = dao.getAll()

    companion object {
        fun fromEntity(entity: WorkoutEntityWrapper, exercises: List<Exercise>) =
            Workout(
                entity.workoutEntity.id,
                Utils.localeFromEntity(entity.workoutEntity.titles),
                entity.workoutEntity.type,
                entity.sections.map { section ->
                    WorkoutSection(
                        section.workoutSectionEntity.id,
                        section.workoutSectionEntity.order,
                        Utils.localeFromEntity(section.workoutSectionEntity.titles),
                        section.setups.map {
                            ExerciseSetupRepository.fromEntity(
                                it,
                                section.workoutSectionEntity.id,
                                exercises.find { ex -> ex.id == it.exerciseId }!!
                            )
                        }
                    )
                }
            )
    }
}