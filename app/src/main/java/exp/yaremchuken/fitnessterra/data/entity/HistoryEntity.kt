package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("history")
data class HistoryEntity(
    @PrimaryKey
    @ColumnInfo("started_at")
    val startedAt: Long,

    @ColumnInfo("finished_at")
    val finishedAt: Long,

    /**
     * JSON description of performed workout.
     */
    @ColumnInfo("workout_data")
    val workoutData: String
)
