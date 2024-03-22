package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutSequenceEntityWrapper(
    @Embedded
    val workoutSequenceEntity: WorkoutSequenceEntity,

    @Relation(
        entity = WorkoutSequenceLinkEntity::class,
        entityColumn = "sequence_id",
        parentColumn = "id"
    )
    val workoutLinks: List<WorkoutSequenceLinkEntityWrapper>
)