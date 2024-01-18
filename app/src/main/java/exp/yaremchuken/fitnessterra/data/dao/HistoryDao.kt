package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HistoryEntity)

    @Delete
    suspend fun delete(entity: HistoryEntity)

    @Query("select * from history where started_at >= :fromMillis and finished_at < :toMillis")
    fun getInPeriod(fromMillis: Long, toMillis: Long): Flow<List<HistoryEntity>>
}