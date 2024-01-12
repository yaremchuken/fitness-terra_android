package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Workout scheduled to be performed at specific time.
 */
@Entity("schedule")
data class ScheduleEntity(
    @PrimaryKey val id: Long?,
    /**
     * The datetime in milliseconds for which the workout is scheduled.
     */
    @ColumnInfo("scheduled_at") val scheduledAt: Long,

    @ColumnInfo("workout_id") val workoutId: Long,

    /**
     * Mark schedule as repeatable, at specific day of week.
     */
    val monday: Boolean = false,
    val tuesday: Boolean = false,
    val wednesday: Boolean = false,
    val thursday: Boolean = false,
    val friday: Boolean = false,
    val saturday: Boolean = false,
    val sunday: Boolean = false
)
