package exp.yaremchuken.fitnessterra.data.repository

import com.google.gson.Gson
import exp.yaremchuken.fitnessterra.data.dao.HistoryDao
import exp.yaremchuken.fitnessterra.data.datasource.dto.shallow.WorkoutShallowDto
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.toInstant
import java.time.Instant
import java.time.LocalDate

class HistoryRepository(
    private val dao: HistoryDao
) {
    suspend fun insert(history: History) = dao.insert(toEntity(history))

    fun getInPeriod(from: LocalDate, to: LocalDate) =
        dao.getInPeriod(
            from.toInstant().toEpochMilli(),
            to.plusDays(1).toInstant().toEpochMilli()
        )

    fun getLatest(limit: Long) = dao.getLatest(limit)

    private fun toEntity(history: History) =
        HistoryEntity(
            history.id,
            history.startedAt.toEpochMilli(),
            history.finishedAt.toEpochMilli(),
            history.workout.id,
            Gson().toJson(WorkoutShallowDto.fromWorkout(history.workout))
        )

    fun fromEntity(entity: HistoryEntity, original: Workout): History {
        val workoutShallowDto = Gson().fromJson(entity.content, WorkoutShallowDto::class.java)
        return History(
            entity.id,
            Instant.ofEpochMilli(entity.startedAt),
            Instant.ofEpochMilli(entity.finishedAt),
            WorkoutShallowDto.toWorkout(workoutShallowDto, original)
        )
    }
}