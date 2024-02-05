package exp.yaremchuken.fitnessterra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import exp.yaremchuken.fitnessterra.data.entity.ExerciseSetupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSetupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExerciseSetupEntity)

    @Query("select * from exercise_setup where section_id = :sectionId and exercise_id = :exerciseId")
    fun getBySectionAndExercise(sectionId: Long, exerciseId: Long): Flow<ExerciseSetupEntity>
}