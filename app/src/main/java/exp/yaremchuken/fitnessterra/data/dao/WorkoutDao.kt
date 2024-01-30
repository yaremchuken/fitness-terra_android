package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WorkoutEntity)

    @Transaction
    @Query("select * from workout where id = :id")
    fun getById(id: Long): Flow<WorkoutEntityWrapper>

    @Transaction
    @Query("select * from workout")
    fun getAll(): Flow<List<WorkoutEntityWrapper>>
}