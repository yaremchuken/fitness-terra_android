package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity("schedule")
data class ScheduleEntity(
    @PrimaryKey val scheduledAt: Long,
    @Nonnull @ColumnInfo("workout_id") val workoutId: Long,
    @Nonnull val completed: Boolean = false
)
