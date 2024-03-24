package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.WorkoutSequenceDao
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntityWrapper
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceLinkEntity
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.WorkoutSequence
import exp.yaremchuken.fitnessterra.util.Utils
import java.time.LocalTime

class WorkoutSequenceRepository(
    private val dao: WorkoutSequenceDao
) {
    suspend fun insert(entity: WorkoutSequenceEntity, links: List<WorkoutSequenceLinkEntity>) =
        dao.insert(entity, links)

    fun getAll() = dao.getAll()

    companion object {
        fun fromEntity(entity: WorkoutSequenceEntityWrapper, exercises: List<Exercise>) =
            WorkoutSequence(
                LocalTime.ofSecondOfDay(entity.workoutSequenceEntity.scheduledAt),
                entity.workoutLinks.map { it.workout }.map { WorkoutRepository.fromEntity(it, exercises) },
                Utils.flagsToWeekdays(
                    listOf(
                        entity.workoutSequenceEntity.monday,
                        entity.workoutSequenceEntity.tuesday,
                        entity.workoutSequenceEntity.wednesday,
                        entity.workoutSequenceEntity.thursday,
                        entity.workoutSequenceEntity.friday,
                        entity.workoutSequenceEntity.saturday,
                        entity.workoutSequenceEntity.sunday
                    )
                ),
                entity.workoutSequenceEntity.daysSpan
            )
    }
}