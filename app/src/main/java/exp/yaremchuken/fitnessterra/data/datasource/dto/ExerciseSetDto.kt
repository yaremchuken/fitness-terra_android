package exp.yaremchuken.fitnessterra.data.datasource.dto

class ExerciseSetDto {
    var exerciseId: Long = -1
    var weight: Long = 0
    var repeats: List<Long> = listOf()
    var duration: Long = 0
    var recovery: Long = 0
}