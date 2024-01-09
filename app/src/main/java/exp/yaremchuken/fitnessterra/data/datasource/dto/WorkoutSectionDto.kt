package exp.yaremchuken.fitnessterra.data.datasource.dto

import java.util.Locale

class WorkoutSectionDto {
    lateinit var title: Map<Locale, String>
    lateinit var sets: List<ExerciseSetDto>
}