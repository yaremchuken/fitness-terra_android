package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ScheduleEntityWrapper(
    @Embedded
    val scheduleEntity: ScheduleEntity,

    @Relation(
        entity = WorkoutEntity::class,
        entityColumn = "id",
        parentColumn = "workout_id"
    )
    val workout: WorkoutEntityWrapper
)
