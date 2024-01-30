package exp.yaremchuken.fitnessterra.data.entity.dto

data class HistoryWorkoutSectionDto(
    val id: Long,
    val order: Long,
    val title: String,
    val setups: List<HistoryExerciseSetupDto>
)
