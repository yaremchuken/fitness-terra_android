package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.ScheduleDao
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.model.Schedule
import java.time.Instant

class ScheduleRepository(
    private val dao: ScheduleDao
) {
    suspend fun insert(schedule: Schedule) = dao.insert(toEntity(schedule))

    suspend fun delete(schedule: Schedule) = dao.delete(toEntity(schedule))

    fun fetchAll() = dao.fetchAll()

    suspend fun markCompleted(scheduledAt: Long) = dao.markCompleted(scheduledAt)

    private fun toEntity(schedule: Schedule) =
        ScheduleEntity(
            schedule.scheduledAt.toEpochMilli(),
            schedule.workout.id,
            schedule.completed
        )

    fun fromEntity(entity: ScheduleEntity, workoutRepository: WorkoutRepository) =
        Schedule(
            Instant.ofEpochMilli(entity.scheduledAt),
            workoutRepository.getById(entity.workoutId)!!,
            entity.completed
        )
}