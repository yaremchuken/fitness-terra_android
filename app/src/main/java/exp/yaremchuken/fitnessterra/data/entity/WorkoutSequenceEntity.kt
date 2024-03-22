package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("workout_sequence")
data class WorkoutSequenceEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("scheduled_at")
    val scheduledAt: Long,

    @ColumnInfo("days_span")
    val daysSpan: Int,

    /**
     * Restricts weekdays on which this sequence can be performed.
     */
    val monday: Boolean = false,
    val tuesday: Boolean = false,
    val wednesday: Boolean = false,
    val thursday: Boolean = false,
    val friday: Boolean = false,
    val saturday: Boolean = false,
    val sunday: Boolean = false
)