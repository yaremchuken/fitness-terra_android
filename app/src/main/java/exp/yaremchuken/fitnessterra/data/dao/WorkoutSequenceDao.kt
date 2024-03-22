package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntityWrapper
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSequenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WorkoutSequenceEntity, links: List<WorkoutSequenceLinkEntity>)

    @Transaction
    @Query("select * from workout_sequence")
    fun getAll(): Flow<List<WorkoutSequenceEntityWrapper>>
}