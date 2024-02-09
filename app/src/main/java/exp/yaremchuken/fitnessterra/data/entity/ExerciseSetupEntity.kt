package exp.yaremchuken.fitnessterra.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("exercise_setup", primaryKeys = ["section_id", "exercise_id", "order"])
data class ExerciseSetupEntity(
    @ColumnInfo("section_id")
    val sectionId: Long,

    /**
     * Exercise which this setup is aimed for.
     */
    @ColumnInfo("exercise_id")
    val exerciseId: Long,

    /**
     * Position of this exercise in section.
     */
    @ColumnInfo("order")
    val order: Long,

    /**
     * Amount of weight used in sets in grams.
     */
    @ColumnInfo("weight")
    val weight: Long,

    /**
     * Amount of repeats for exercise.
     * Not needed if the duration is specified.
     */
    @ColumnInfo("sets")
    val sets: String,

    /**
     * Duration of exercise performing in seconds.
     * Not needed if the repeats is specified.
     */
    @ColumnInfo("duration")
    val duration: Long,

    /**
     * Amount ot time to rest after performing this setup in seconds.
     */
    @ColumnInfo("recovery")
    val recovery: Long
)
