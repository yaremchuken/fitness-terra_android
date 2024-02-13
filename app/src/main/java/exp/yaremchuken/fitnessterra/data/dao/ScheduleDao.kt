package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntityWrapper
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ScheduleEntity)

    @Delete
    suspend fun delete(entity: ScheduleEntity)

    @Query(
        """
            select * from schedule
            where (scheduled_at >= :fromMillis and scheduled_at < :toMillis)
            or (monday = 1 and monday = :mon)
            or (tuesday = 1 and tuesday = :tue)
            or (wednesday = 1 and wednesday = :wed)
            or (thursday = 1 and thursday = :thu)
            or (friday = 1 and friday = :fri)
            or (saturday = 1 and saturday = :sat)
            or (sunday = 1 and sunday = :sun)
        """)
    fun getInPeriod(
        fromMillis: Long, toMillis: Long,
        mon: Boolean, tue: Boolean, wed: Boolean, thu: Boolean, fri: Boolean, sat: Boolean, sun: Boolean
    ): Flow<List<ScheduleEntityWrapper>>
}