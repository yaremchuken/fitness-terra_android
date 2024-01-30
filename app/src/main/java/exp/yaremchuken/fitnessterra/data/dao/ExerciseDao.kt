package exp.yaremchuken.fitnessterra.data.dao

import exp.yaremchuken.fitnessterra.data.datasource.YamlDatasource
import exp.yaremchuken.fitnessterra.data.model.Exercise

class ExerciseDao(
    private val datasource: YamlDatasource
) {
    fun getByIds(ids: List<Long>): List<Exercise> =
        datasource.exercises.filter { ids.contains(it.id) }.map { it.copy() }

    fun getAll(): List<Exercise> = datasource.exercises.map { it.copy() }
}