package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutSectionEntityWrapper(
    @Embedded
    val workoutSectionEntity: WorkoutSectionEntity,

    @Relation(
        entity = ExerciseSetupEntity::class,
        entityColumn = "section_id",
        parentColumn = "id"
    )
    val setups: List<ExerciseSetupEntity>
)
