package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSectionEntity

@Dao
interface WorkoutSectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WorkoutSectionEntity)
}