package exp.yaremchuken.fitnessterra.data.dao

import exp.yaremchuken.fitnessterra.data.datasource.YamlDatasource
import exp.yaremchuken.fitnessterra.data.model.Exercise

class ExerciseDao(
    private val datasource: YamlDatasource
) {
    fun getById(id: Long): Exercise? = datasource.exercises.find { it.id == id }?.copy()
    fun getAll(): List<Exercise> = datasource.exercises.map { it.copy() }
}