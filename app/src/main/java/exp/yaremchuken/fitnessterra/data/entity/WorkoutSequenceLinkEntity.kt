package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("workout_sequence_link", primaryKeys = ["sequence_id", "workout_id"])
data class WorkoutSequenceLinkEntity(
    @ColumnInfo("sequence_id")
    val sequenceId: Long,

    @ColumnInfo("workout_id")
    val workoutId: Long
)