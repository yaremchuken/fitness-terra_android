package exp.yaremchuken.fitnessterra.data.entity.dto

import exp.yaremchuken.fitnessterra.data.model.Equipment

data class HistoryExerciseSetupDto(
    val sectionId: Long,
    val exerciseId: Long,
    val order: Long,
    val equipment: List<Equipment>,
    val sets: String,

    /**
     * Exercise duration in seconds.
     */
    val duration: Long,

    /**
     * Exercise recovery time in seconds.
     */
    val recovery: Long
)
