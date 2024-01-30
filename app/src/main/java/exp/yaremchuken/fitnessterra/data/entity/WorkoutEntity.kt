package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import exp.yaremchuken.fitnessterra.data.model.WorkoutType

@Entity("workout")
data class WorkoutEntity(
    @PrimaryKey
    val id: Long,

    @ColumnInfo("type")
    val type: WorkoutType,

    @ColumnInfo("titles")
    val titles: String
)
