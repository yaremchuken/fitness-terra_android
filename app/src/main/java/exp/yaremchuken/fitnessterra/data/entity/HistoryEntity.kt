package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("history")
data class HistoryEntity(
    @PrimaryKey val id: Long?,

    @ColumnInfo("started_at") val startedAt: Long,
    @ColumnInfo("finished_at") val finishedAt: Long,

    @ColumnInfo("workout_id") val workoutId: Long,

    /**
     * JSON description of exercise where some stuff like weight, repeats, sets etc may be modified
     */
    @ColumnInfo("content") val content: String
)
