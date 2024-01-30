package exp.yaremchuken.fitnessterra.data.repository

import com.google.gson.Gson
import exp.yaremchuken.fitnessterra.data.dao.HistoryDao
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.dto.HistoryWorkoutDto
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.History
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
            history.startedAt.toEpochMilli(),
            history.finishedAt.toEpochMilli(),
            Gson().toJson(HistoryWorkoutDto.fromWorkout(history.workout))
        )

    fun fromEntity(entity: HistoryEntity, exercises: List<Exercise>): History {
        val historyWorkoutDto = Gson().fromJson(entity.workoutData, HistoryWorkoutDto::class.java)
        return History(
            Instant.ofEpochMilli(entity.startedAt),
            Instant.ofEpochMilli(entity.finishedAt),
            historyWorkoutDto.toWorkout(exercises)
        )
    }
}