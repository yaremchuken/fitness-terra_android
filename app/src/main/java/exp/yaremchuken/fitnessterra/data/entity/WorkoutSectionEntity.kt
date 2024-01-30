package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("workout_section")
data class WorkoutSectionEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("workout_id")
    val workoutId: Long,

    @ColumnInfo("order")
    val order: Long,

    @ColumnInfo("titles")
    val titles: String
)