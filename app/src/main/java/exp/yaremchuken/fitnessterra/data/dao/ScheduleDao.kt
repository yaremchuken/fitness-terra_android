package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(entity: ScheduleEntity)

    @Delete
    suspend fun delete(entity: ScheduleEntity)

    @Query("select * from schedule")
    fun fetchAll(): Flow<List<ScheduleEntity>>

    @Query("update schedule set completed = 1 where scheduledAt = :scheduledAt")
    suspend fun markCompleted(scheduledAt: Long)
}