package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutEntityWrapper(
    @Embedded
    val workoutEntity: WorkoutEntity,

    @Relation(
        entity = WorkoutSectionEntity::class,
        entityColumn = "workout_id",
        parentColumn = "id"
    )
    val sections: List<WorkoutSectionEntityWrapper>
)
