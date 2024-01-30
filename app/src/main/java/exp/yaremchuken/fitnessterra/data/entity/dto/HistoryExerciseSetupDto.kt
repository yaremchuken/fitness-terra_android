package exp.yaremchuken.fitnessterra.data.entity.dto

data class HistoryExerciseSetupDto(
    val sectionId: Long,
    val exerciseId: Long,
    val weight: Long,
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
